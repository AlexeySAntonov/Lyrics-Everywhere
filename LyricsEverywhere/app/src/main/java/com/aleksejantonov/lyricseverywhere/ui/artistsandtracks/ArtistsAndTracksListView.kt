package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks

import com.aleksejantonov.lyricseverywhere.ui.base.BaseView
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ArtistsAndTracksListView : BaseView {
  fun showData(items: List<ListItem>)
  fun showLoading()
  fun hideLoading()
}
