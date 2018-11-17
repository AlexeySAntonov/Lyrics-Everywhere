package com.aleksejantonov.lyricseverywhere.ui.advancedsearch

import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AdvancedSearchView : MvpView {
  fun showItems(items: List<ListItem>)
}