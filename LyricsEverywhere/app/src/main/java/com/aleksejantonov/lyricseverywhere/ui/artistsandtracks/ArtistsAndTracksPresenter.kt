package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks

import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.sl.SL
import com.aleksejantonov.lyricseverywhere.ui.base.BasePresenter
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTrackClick
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTwitterClick
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.entity.LoadingItem
import com.aleksejantonov.lyricseverywhere.utils.DataContainersUtil
import com.aleksejantonov.lyricseverywhere.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@InjectViewState
class ArtistsAndTracksPresenter : BasePresenter<ArtistsAndTracksListView>() {

  private companion object {
    const val PAGE_NUMBER = "1"
    const val PAGE_SIZE = "100"
  }

  private var artists: List<Artist> = ArrayList()
  private var tracks: List<Track> = ArrayList()
  private val musixMatchService = SL.componentManager().appComponent.service
  private var country: String? = null
  private val dataBase = SL.componentManager().appComponent.dataBase
  private val preferences = SL.componentManager().appComponent.preferences

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadData()

    viewActions
        .subscribeBy { handleAction(it as Action) }
        .keepUntilDestroy()
  }

  private fun handleAction(action: Action) {
    when (action) {
      is OnTwitterClick -> viewState.showTwitter(action.twitterUrl)
      is OnTrackClick   -> viewState.showTrackDetails(action.track, action.sharedImageView)
    }
  }

  fun setCountry(country: String) {
    this.country = country
  }

  fun loadData() {
    if (country == null) country = QueryType.RU.name

    Single
        .fromCallable {
          dataBase.getCountryArtists(country)
        }
        .flatMap {
          if (it.isNotEmpty()) {
            artists = it
            Single.fromCallable { dataBase.tracks }
          } else {
            Single.just(emptyList())
          }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showItems(listOf(LoadingItem())) }
        .subscribe(
            {
              if (it.isNotEmpty()) {
                tracks = it
                viewState.showItems(DataMergeUtil.listsMerge(artists, tracks))
              } else {
                loadArtists()
              }
            },
            { Timber.e("Data loading failed: ${it.message}") }
        )
        .keepUntilDestroy()
  }

  fun loadArtists() {
    musixMatchService
        .getArtists(preferences.getString(Constants.API_KEY, ""), country, PAGE_NUMBER, PAGE_SIZE)
        .flatMapCompletable {
          Completable.fromAction {
            artists = DataContainersUtil.artistContainersToArtists(it.message.body.artistContainers, country!!)
            dataBase.insertArtists(artists)
          }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete { loadTracks() }
        .doOnSubscribe { viewState.showItems(listOf(LoadingItem())) }
        .subscribeBy(
            onComplete = { Timber.d("Artists loading succeed") },
            onError = { Timber.d("Artists loading failed: ${it.message}") }
        )
        .keepUntilDestroy()
  }

  private fun loadTracks() {
    musixMatchService
        .getTracks(preferences.getString(Constants.API_KEY, ""), country, PAGE_NUMBER, PAGE_SIZE)
        .flatMapCompletable {
          Completable.fromAction {
            tracks = DataContainersUtil.trackContainersToTracks(it.message.body.trackContainers)
            dataBase.insertTracks(tracks)
          }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnComplete { loadAlbums() }
        .subscribeBy(
            onComplete = { viewState.showItems(DataMergeUtil.listsMerge(artists, tracks)) },
            onError = { Timber.d("Tracks loading failed: ${it.message}") }
        )
        .keepUntilDestroy()
  }

  private fun loadAlbums() {
    Observable
        .fromIterable(artists)
        .flatMapSingle {
          musixMatchService.getAlbums(preferences.getString(Constants.API_KEY, ""), it.artistId)
        }
        .flatMapCompletable {
          Completable.fromAction {
            dataBase.insertAlbums(DataContainersUtil.albumContainersToAlbums(it.message.body.albumsContainers))
          }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(
            onComplete = {},
            onError = Timber::e
        )
        .keepUntilDestroy()
  }
}
