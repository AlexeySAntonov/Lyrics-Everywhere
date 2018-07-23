package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ArtistsAndTracksListView : MvpView {
  fun showData(data: List<BaseData>)
  fun showLoading()
  fun hideLoading()
}
