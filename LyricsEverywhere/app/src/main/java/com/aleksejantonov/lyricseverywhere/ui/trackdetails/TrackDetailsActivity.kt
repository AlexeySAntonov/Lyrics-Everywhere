package com.aleksejantonov.lyricseverywhere.ui.trackdetails

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_AUTO
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES
import android.support.v7.app.AppCompatDelegate.getDefaultNightMode
import android.view.View
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.utils.DateTimeUtil
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_track_details.albumCover
import kotlinx.android.synthetic.main.activity_track_details.lyrics
import kotlinx.android.synthetic.main.activity_track_details.progressBar
import kotlinx.android.synthetic.main.activity_track_details.textDivider
import kotlinx.android.synthetic.main.activity_track_details.toolbar
import kotlinx.android.synthetic.main.activity_track_details.trackAlbum
import kotlinx.android.synthetic.main.activity_track_details.trackName
import android.os.Build
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class TrackDetailsActivity : MvpAppCompatActivity(), TrackDetailsView {
  companion object {
    private val EXTRA_TRACK = TrackDetailsActivity::class.java.simpleName + "track"
    private val EXTRA_IMAGE_TRANSITION_NAME = TrackDetailsActivity::class.java.simpleName + "transition image"

    fun newIntent(context: Context, track: Track, transitionName: String?) = Intent(context, TrackDetailsActivity::class.java).apply {
      putExtra(EXTRA_TRACK, track)
      putExtra(EXTRA_IMAGE_TRANSITION_NAME, transitionName)
    }
  }

  private lateinit var track: Track

  @InjectPresenter
  lateinit var presenter: TrackDetailsPresenter

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_track_details)
    supportPostponeEnterTransition()

    track = intent.getSerializableExtra(EXTRA_TRACK) as Track
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      val imageTransitionName = intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME)
      albumCover.transitionName = imageTransitionName
    }


    setSupportActionBar(toolbar)
    supportActionBar?.title = getString(R.string.lyrics_of) + "\"" + track.trackName + "\""
    toolbar.apply {
      setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
      setNavigationOnClickListener { onBackPressed() }
    }

    if (getDefaultNightMode() == MODE_NIGHT_YES || (getDefaultNightMode() == MODE_NIGHT_AUTO && DateTimeUtil.isNightModeNecessary())) {
      textDivider.setBackgroundColor(ContextCompat.getColor(this, R.color.defaultGrayColor))
      albumCover.alpha = 0.5f
    }

    trackName.text = track.trackName
    trackAlbum.text = String.format(getString(R.string.album), track.albumName)
    Glide.with(this)
        .load(track.albumCover)
        .listener(object : RequestListener<Drawable> {
          override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            supportStartPostponedEnterTransition()
            return false
          }

          override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            supportStartPostponedEnterTransition()
            return false
          }
        })
        .into(albumCover)

    presenter.setTrackId(track.trackId.toString())
  }

  override fun showData(lyricsText: String) {
    lyrics.text = lyricsText
  }

  override fun showLoading() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progressBar.visibility = View.GONE
  }
}
