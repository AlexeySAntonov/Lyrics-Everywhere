package com.aleksejantonov.lyricseverywhere.ui.trackdetails

import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.sl.SL
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class TrackDetailsPresenter : MvpPresenter<TrackDetailsView>() {

  private lateinit var trackId: String
  private val musixMatchService = SL.componentManager().appComponent.service
  private val preferences = SL.componentManager().appComponent.preferences
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
        .doOnSubscribe { viewState.showLoading() }
        .doAfterTerminate { viewState.hideLoading() }
        .subscribe(
            {
              viewState.showData(it.message.body.lyrics.lyricsText)
            },
            { Timber.e("Lyrics loading failed: ${it.message}") }
        )
    )
  }
}
