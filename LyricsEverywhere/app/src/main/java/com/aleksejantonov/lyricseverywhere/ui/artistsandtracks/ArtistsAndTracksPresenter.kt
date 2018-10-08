package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks

import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.base.BasePresenter
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTrackClick
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTwitterClick
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType
import com.aleksejantonov.lyricseverywhere.utils.DataContainersUtil
import com.aleksejantonov.lyricseverywhere.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList

@InjectViewState
class ArtistsAndTracksPresenter : BasePresenter<ArtistsAndTracksListView>() {

  private companion object {
    const val PAGE_NUMBER = "1"
    const val PAGE_SIZE = "100"
  }

  private var artists: List<Artist> = ArrayList()
  private var tracks: List<Track> = ArrayList()
  private val musixMatchService = DI.componentManager().appComponent.service
  private var country: String? = null
  private val dataBase = DI.componentManager().appComponent.dataBase
  private val preferences = DI.componentManager().appComponent.preferences

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
      is OnTrackClick   -> viewState.showTrackDetails(action.track)
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
        .doOnSubscribe { viewState.showLoading() }
        .doAfterTerminate { viewState.hideLoading() }
        .subscribe(
            {
              if (it.isNotEmpty()) {
                tracks = it
                viewState.showData(DataMergeUtil.listsMerge(artists, tracks))
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
        .doOnSubscribe { viewState.showLoading() }
        .doAfterTerminate { viewState.hideLoading() }
        .subscribe(
            { Timber.d("Artists loading succeed") },
            { Timber.d("Artists loading failed: ${it.message}") }
        )
        .keepUntilDestroy()
  }

  @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
        .doOnSubscribe { viewState.showLoading() }
        .doAfterTerminate { viewState.hideLoading() }
        .subscribe(
            { viewState.showData(DataMergeUtil.listsMerge(artists, tracks)) },
            { Timber.d("Tracks loading failed: ${it.message}") }
        )
        .keepUntilDestroy()
  }
}
