package com.aleksejantonov.lyricseverywhere.ui.search

import com.aleksejantonov.lyricseverywhere.ui.base.BaseView
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SearchFragmentView : BaseView {
  fun showData(items: List<ListItem>, query: String)
  fun setQuery(query: String)
  fun showLoading()
  fun hideLoading()
}
