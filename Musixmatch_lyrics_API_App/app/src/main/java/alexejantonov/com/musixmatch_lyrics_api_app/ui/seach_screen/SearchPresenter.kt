package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class SearchPresenter : MvpPresenter<SearchFragmentView>() {

  private val dataBase = MyApplication.dataBase

  fun loadData(query: String) {
    val baseData = DataMergeUtil.searchListsMerge(dataBase.allArtist, dataBase.tracks, dataBase.getQueryData(query))
    viewState.showData(baseData, query)
  }
}
