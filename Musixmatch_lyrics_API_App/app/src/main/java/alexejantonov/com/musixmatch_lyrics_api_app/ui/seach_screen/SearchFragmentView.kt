package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchFragmentView : MvpView {
  fun showData(data: List<BaseData>, query: String)
}
