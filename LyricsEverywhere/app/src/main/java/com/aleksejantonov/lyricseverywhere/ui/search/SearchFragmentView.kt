package com.aleksejantonov.lyricseverywhere.ui.search

import com.aleksejantonov.lyricseverywhere.ui.base.BaseData
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchFragmentView : MvpView {
  fun showData(data: List<BaseData>, query: String)
  fun showLoading()
  fun hideLoading()
}
