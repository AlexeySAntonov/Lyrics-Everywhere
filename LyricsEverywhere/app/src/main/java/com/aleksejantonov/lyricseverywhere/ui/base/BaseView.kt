package com.aleksejantonov.lyricseverywhere.ui.base

import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {
  @StateStrategyType(SkipStrategy::class)
  fun showTwitter(twitterUrl: String)
  @StateStrategyType(SkipStrategy::class)
  fun showTrackDetails(track: Track)

  sealed class Action {
    data class OnTwitterClick(val twitterUrl: String) : Action()
    data class OnTrackClick(val track: Track) : Action()
  }
}
