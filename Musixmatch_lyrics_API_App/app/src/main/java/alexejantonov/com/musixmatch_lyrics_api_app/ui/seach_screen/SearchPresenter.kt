package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil
import android.util.Log
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
