package com.aleksejantonov.lyricseverywhere.ui.base

import android.widget.ImageView
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {
  @StateStrategyType(SkipStrategy::class)
  fun showTwitter(twitterUrl: String)
  @StateStrategyType(SkipStrategy::class)
  fun showTrackDetails(track: Track, sharedImageView: ImageView)

  sealed class Action {
    data class OnTwitterClick(val twitterUrl: String) : Action()
    data class OnTrackClick(val track: Track, val sharedImageView: ImageView) : Action()
    data class OnSearchClick(val query: String): Action()
  }
}
