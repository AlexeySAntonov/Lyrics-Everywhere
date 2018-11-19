package com.aleksejantonov.lyricseverywhere.ui.advancedsearch

import com.aleksejantonov.lyricseverywhere.ui.base.BaseView
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AdvancedSearchView : BaseView {
  fun showItems(items: List<ListItem>)
}