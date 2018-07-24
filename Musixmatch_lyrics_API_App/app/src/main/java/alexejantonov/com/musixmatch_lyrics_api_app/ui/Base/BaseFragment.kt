package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base

import alexejantonov.com.musixmatch_lyrics_api_app.MainActivity
import alexejantonov.com.musixmatch_lyrics_api_app.R
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.ScreenType.COUNTRY
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.ScreenType.SEARCH
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.ScreenType.SETTINGS
import alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksListFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen.SearchFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.settings.SettingsFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.arellomobile.mvp.MvpAppCompatFragment

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
      Log.d("twitter", "twitter://user?screen_name=" + twitterUrl.substring(20))
      try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterUrl.substring(20))))
      } catch (e: Exception) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)))
      }
    } else {
      view?.let { Snackbar.make(it, R.string.no_twitter, Snackbar.LENGTH_LONG).show() }
    }
  }
}
