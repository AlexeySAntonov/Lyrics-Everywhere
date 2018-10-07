package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks

import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ArtistsAndTracksListView : MvpView {
  fun showData(items: List<ListItem>)
  fun showLoading()
  fun hideLoading()

  @StateStrategyType(SkipStrategy::class)
  fun showTwitter(twitterUrl: String)
  @StateStrategyType(SkipStrategy::class)
  fun showTrackDetails(track: Track)

  sealed class Action {
    data class OnTwitterClick(val twitterUrl: String) : Action()
    data class OnTrackClick(val track: Track) : Action()
  }
}
