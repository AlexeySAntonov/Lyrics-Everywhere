package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base

import alexejantonov.com.musixmatch_lyrics_api_app.MainActivity
import alexejantonov.com.musixmatch_lyrics_api_app.R
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track
import alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksListFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen.SearchFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import com.arellomobile.mvp.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment() {

  companion object {
    fun newInstance(type: FragmentType, query: QueryType): BaseFragment {
      return when (type) {
        FragmentType.COUNTRY -> ArtistsAndTracksListFragment.newInstance(query)
        FragmentType.SEARCH  -> SearchFragment.newInstance()
      }
    }
  }

  lateinit var activity: MainActivity
  var queryTitle: String? = null

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

  fun setToolbarTitle(query: QueryType) {
    activity.supportActionBar?.title = when (query) {
      QueryType.RU             -> getText(R.string.russian_top_chart)
      QueryType.US             -> getText(R.string.usa_top_chart)
      QueryType.GB             -> getText(R.string.britain_top_chart)
      QueryType.SEARCH         -> "Results for query \"$queryTitle \""
      QueryType.DEFAULT_SEARCH -> "Do a Search"
    }
  }
}
