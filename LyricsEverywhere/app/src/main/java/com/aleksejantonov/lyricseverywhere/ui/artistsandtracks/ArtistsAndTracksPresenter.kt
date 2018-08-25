package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks

import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType
import com.aleksejantonov.lyricseverywhere.utils.DataContainersUtil
import com.aleksejantonov.lyricseverywhere.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.ArrayList

@InjectViewState
class ArtistsAndTracksPresenter : MvpPresenter<ArtistsAndTracksListView>() {

  private var artists: List<Artist> = ArrayList()
  private var tracks: List<Track> = ArrayList()
  private val musixMatchService = DI.componentManager().appComponent.service
  private var country: String? = null
  private val dataBase = DI.componentManager().appComponent.dataBase
  private val subscriptions = CompositeDisposable()
  private val preferences = DI.componentManager().appComponent.preferences

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadData()
  }

  fun setCountry(country: String) {
    this.country = country
  }

  fun loadData() {
    if (country == null) {
      country = QueryType.RU.name
    }
    subscriptions.add(
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
    )
  }

  fun loadArtists() {
    subscriptions.add(
        musixMatchService
            .getArtists(preferences.getString(Constants.API_KEY, ""), country, "1", "100")
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
    )
  }

  private fun loadTracks() {
    subscriptions.add(
        musixMatchService
            .getTracks(preferences.getString(Constants.API_KEY, ""), country, "1", "100")
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
    )
  }

  override fun detachView(view: ArtistsAndTracksListView) {
    subscriptions.clear()
    super.detachView(view)
  }
}
