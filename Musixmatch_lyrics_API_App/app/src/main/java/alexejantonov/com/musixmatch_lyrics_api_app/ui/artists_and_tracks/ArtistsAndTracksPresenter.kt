package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataContainersUtil
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Completable
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
    if (dataBase.getCountryArtists(country).isNotEmpty() && dataBase.tracks.isNotEmpty()) {
      viewState.showData(DataMergeUtil.listsMerge(dataBase.getCountryArtists(country), dataBase.tracks))
    } else {
      loadArtists()
    }
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
            .doOnComplete { loadTracks() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
