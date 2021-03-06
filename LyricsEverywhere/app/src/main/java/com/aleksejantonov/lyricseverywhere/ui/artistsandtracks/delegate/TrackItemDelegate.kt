package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate

import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTrackClick
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.utils.DateTimeUtil
import com.aleksejantonov.lyricseverywhere.utils.highlight
import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.jakewharton.rxrelay2.PublishRelay
import kotlinx.android.synthetic.main.item_track.view.albumCover
import kotlinx.android.synthetic.main.item_track.view.sound
import kotlinx.android.synthetic.main.item_track.view.trackAlbum
import kotlinx.android.synthetic.main.item_track.view.trackName

class TrackItemDelegate(
    private var query: String? = null,
    private val imageRequestManager: RequestManager,
    private val viewActions: PublishRelay<Any>
) : AbsListItemAdapterDelegate<Track, ListItem, TrackItemDelegate.TrackViewHolder>() {

  override fun isForViewType(item: ListItem, items: List<ListItem>, position: Int) = item is Track

  override fun onCreateViewHolder(parent: ViewGroup) =
      TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false))

  override fun onBindViewHolder(item: Track, viewHolder: TrackViewHolder, payloads: List<Any>) {
    viewHolder.bind(item)
  }

  fun setQuery(query: String) {
    this.query = query
  }

  inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(track: Track) {
      itemView.apply {
        trackName.text = track.trackName
        query?.let { track.trackName.highlight(it, context)?.let { trackName.text = it } }

        trackAlbum.text = track.albumName
        imageRequestManager.load(Constants.ALBUM_STUB).into(albumCover)

        ViewCompat.setTransitionName(albumCover as View, track.trackName)

        setOnClickListener { viewActions.accept(OnTrackClick(track, albumCover)) }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_AUTO && DateTimeUtil.isNightModeNecessary())) {
          sound.alpha = 0.5f
          albumCover.alpha = 0.5f
        }
      }
    }
  }
}