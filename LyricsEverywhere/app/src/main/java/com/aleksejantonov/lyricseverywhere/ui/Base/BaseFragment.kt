package com.aleksejantonov.lyricseverywhere.ui.Base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.aleksejantonov.lyricseverywhere.ui.MainActivity
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType.COUNTRY
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType.SEARCH
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType.SETTINGS
import com.aleksejantonov.lyricseverywhere.ui.artists_and_tracks.ArtistsAndTracksListFragment
import com.aleksejantonov.lyricseverywhere.ui.seach_screen.SearchFragment
import com.aleksejantonov.lyricseverywhere.ui.settings.SettingsFragment
import com.aleksejantonov.lyricseverywhere.ui.track_details.TrackDetailsActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import timber.log.Timber

abstract class BaseFragment : MvpAppCompatFragment() {

  companion object {
    fun newInstance(type: ScreenType, query: QueryType): BaseFragment {
      return when (type) {
        COUNTRY  -> ArtistsAndTracksListFragment.newInstance(query)
        SEARCH   -> SearchFragment.newInstance()
        SETTINGS -> SettingsFragment.newInstance()
      }
    }
  }

  lateinit var activity: MainActivity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity = getActivity() as MainActivity
  }

  fun launchTrackDetailsActivity(track: Track) = context?.let { startActivity(TrackDetailsActivity.newIntent(it, track)) }

  fun launchTwitter(twitterUrl: String) {
    if (twitterUrl.isNotEmpty()) {
      Timber.d("twitter://user?screen_name=${twitterUrl.substring(20)}")
      try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=${twitterUrl.substring(20)}")))
      } catch (e: Exception) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)))
      }
    } else {
      view?.let { Snackbar.make(it, R.string.no_twitter, Snackbar.LENGTH_LONG).show() }
    }
  }
}
