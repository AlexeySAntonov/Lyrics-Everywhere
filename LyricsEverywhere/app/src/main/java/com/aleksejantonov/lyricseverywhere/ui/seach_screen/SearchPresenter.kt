package com.aleksejantonov.lyricseverywhere.ui.seach_screen

import android.util.Log
import com.aleksejantonov.lyricseverywhere.MyApplication
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.Base.BaseData
import com.aleksejantonov.lyricseverywhere.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

@InjectViewState
class SearchPresenter : MvpPresenter<SearchFragmentView>() {

  private val dataBase = MyApplication.dataBase
  private val subscriptions = CompositeDisposable()

  fun loadData(query: String) {
    subscriptions.add(
        Observable
            .combineLatest(
                Observable.fromCallable { dataBase.allArtist },
                Observable.fromCallable { dataBase.tracks.toMutableList() },
                BiFunction<List<Artist>, MutableList<Track>, List<BaseData>> { artists, tracks ->
                  DataMergeUtil.searchListsMerge(artists, tracks, query)
                }
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewState.showData(it, query) },
                { Log.e("Search failed: ", Log.getStackTraceString(it)) }
            )
    )
  }

  override fun onDestroy() {
    subscriptions.clear()
    super.onDestroy()
  }
}
