package alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class TrackDetailsPresenter : MvpPresenter<TrackDetailsView>() {

  private lateinit var trackId: String
  private val musixMatchService = MyApplication.service
  private val preferences = MyApplication.preferences
  private val subscriptions = CompositeDisposable()

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()
    loadLyrics()
  }

  override fun onDestroy() {
    subscriptions.clear()
    super.onDestroy()
  }

  fun setTrackId(trackId: String) {
    this.trackId = trackId
  }

  private fun loadLyrics() {
    subscriptions.add(musixMatchService.getLyrics(preferences.getString(Constants.API_KEY, ""), trackId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            {
              viewState.showData(it.message.body.lyrics.lyricsText)
            },
            { Log.e("Lyrics loading failed", Log.getStackTraceString(it)) }
        )
    )
  }
}
