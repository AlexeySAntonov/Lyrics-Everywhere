package com.aleksejantonov.lyricseverywhere.ui.artists_and_tracks

import android.util.Log
import com.aleksejantonov.lyricseverywhere.MyApplication
import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType
import com.aleksejantonov.lyricseverywhere.utils.DataContainersUtil
import com.aleksejantonov.lyricseverywhere.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

@InjectViewState
class ArtistsAndTracksPresenter : MvpPresenter<ArtistsAndTracksListView>() {

  private var artists: List<Artist> = ArrayList()
  private var tracks: List<Track> = ArrayList()
  private val musixMatchService = MyApplication.service
  private var country: String? = null
  private val dataBase = MyApplication.dataBase
  private val subscriptions = CompositeDisposable()
  private val preferences = MyApplication.preferences

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
                { Log.e("Data loading failed", Log.getStackTraceString(it)) }
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
                { Log.d("Artists loading", "succeed") },
                { Log.d("Artists loading failed", Log.getStackTraceString(it)) }
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
                { Log.d("Tracks loading failed", Log.getStackTraceString(it)) }
            )
    )
  }

  override fun detachView(view: ArtistsAndTracksListView) {
    subscriptions.clear()
    super.detachView(view)
  }
}
