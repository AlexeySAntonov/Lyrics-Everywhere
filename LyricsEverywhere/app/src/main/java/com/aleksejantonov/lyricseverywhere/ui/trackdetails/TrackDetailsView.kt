package com.aleksejantonov.lyricseverywhere.ui.trackdetails

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TrackDetailsView : MvpView {
  fun showData(lyricsText: String)
  fun showLoading()
  fun hideLoading()
}
