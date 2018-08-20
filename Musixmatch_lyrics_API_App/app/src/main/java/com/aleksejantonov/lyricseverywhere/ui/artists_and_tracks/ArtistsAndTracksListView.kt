package com.aleksejantonov.lyricseverywhere.ui.artists_and_tracks

import com.aleksejantonov.lyricseverywhere.ui.Base.BaseData
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ArtistsAndTracksListView : MvpView {
  fun showData(data: List<BaseData>)
  fun showLoading()
  fun hideLoading()
}
