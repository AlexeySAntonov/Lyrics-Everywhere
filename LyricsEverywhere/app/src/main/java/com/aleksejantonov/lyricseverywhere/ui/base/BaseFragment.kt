package com.aleksejantonov.lyricseverywhere.ui.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.ui.MainActivity
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.base.ScreenType.SEARCH
import com.aleksejantonov.lyricseverywhere.ui.base.ScreenType.SETTINGS
import com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.ArtistsAndTracksListFragment
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType.RU
import com.aleksejantonov.lyricseverywhere.ui.base.ScreenType.CHART
import com.aleksejantonov.lyricseverywhere.ui.search.SearchFragment
import com.aleksejantonov.lyricseverywhere.ui.settings.SettingsFragment
import com.aleksejantonov.lyricseverywhere.ui.trackdetails.TrackDetailsActivity
import com.arellomobile.mvp.MvpAppCompatFragment
import timber.log.Timber
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.widget.ImageView
import com.aleksejantonov.lyricseverywhere.ui.advancedsearch.AdvancedSearchFragment
import com.aleksejantonov.lyricseverywhere.ui.base.ScreenType.ADVANCED_SEARCH

abstract class BaseFragment : MvpAppCompatFragment() {

  companion object {
    fun newInstance(type: ScreenType, query: QueryType = RU): BaseFragment {
      return when (type) {
        CHART           -> ArtistsAndTracksListFragment.newInstance(query)
        SEARCH          -> SearchFragment.newInstance()
        SETTINGS        -> SettingsFragment.newInstance()
        ADVANCED_SEARCH -> AdvancedSearchFragment.newInstance()
      }
    }
  }

  @get:LayoutRes
  protected abstract val layoutRes: Int

  protected lateinit var activity: MainActivity

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity = getActivity() as MainActivity
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View = inflater.inflate(layoutRes, container, false)

  fun launchTrackDetailsActivity(track: Track, sharedImageView: ImageView) {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
        activity,
        sharedImageView,
        ViewCompat.getTransitionName(sharedImageView) ?: ""
    )

    context?.let {
      startActivity(TrackDetailsActivity.newIntent(it, track, ViewCompat.getTransitionName(sharedImageView)), options.toBundle())
    }
  }

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
