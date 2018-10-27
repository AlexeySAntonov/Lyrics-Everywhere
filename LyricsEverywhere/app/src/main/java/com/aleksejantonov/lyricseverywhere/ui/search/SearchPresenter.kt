package com.aleksejantonov.lyricseverywhere.ui.search

import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.base.BaseData
import com.aleksejantonov.lyricseverywhere.ui.base.BasePresenter
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTrackClick
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTwitterClick
import com.aleksejantonov.lyricseverywhere.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class SearchPresenter : BasePresenter<SearchFragmentView>() {

  private val dataBase = DI.componentManager().appComponent.dataBase

  override fun onFirstViewAttach() {
    super.onFirstViewAttach()

    viewActions
        .subscribeBy { handleAction(it as Action) }
        .keepUntilDestroy()
  }

  fun loadData(query: String) {
    Observable
        .combineLatest(
            Observable.fromCallable { dataBase.allArtist },
            Observable.fromCallable { dataBase.tracks.toMutableList() },
            BiFunction<List<Artist>, MutableList<Track>, List<BaseData>> { artists, tracks ->
              DataMergeUtil.searchListsMerge(artists, tracks, query)
            }
        )
        .doOnSubscribe { viewState.setQuery(query) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { viewState.showData(it, query) },
            { Timber.e("Search failed: ${it.message}") }
        )
        .keepUntilDestroy()
  }

  private fun handleAction(action: Action) {
    when (action) {
      is OnTwitterClick -> viewState.showTwitter(action.twitterUrl)
      is OnTrackClick   -> viewState.showTrackDetails(action.track)
    }
  }
}
