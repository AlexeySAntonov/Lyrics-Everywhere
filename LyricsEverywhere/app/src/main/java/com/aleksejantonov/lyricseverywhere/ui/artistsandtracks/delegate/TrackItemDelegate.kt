package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate

import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTrackClick
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.utils.DateTimeUtil
import com.bumptech.glide.RequestManager
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.jakewharton.rxrelay2.PublishRelay
import kotlinx.android.synthetic.main.item_track.view.albumCover
import kotlinx.android.synthetic.main.item_track.view.sound
import kotlinx.android.synthetic.main.item_track.view.trackAlbum
import kotlinx.android.synthetic.main.item_track.view.trackName

class TrackItemDelegate(
    private val query: String? = null,
    private val imageRequestManager: RequestManager,
    private val viewActions: PublishRelay<Any>
) : AbsListItemAdapterDelegate<Track, ListItem, TrackItemDelegate.TrackViewHolder>() {

  override fun isForViewType(item: ListItem, items: List<ListItem>, position: Int) = item is Track

  override fun onCreateViewHolder(parent: ViewGroup) =
      TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false))

  override fun onBindViewHolder(item: Track, viewHolder: TrackViewHolder, payloads: List<Any>) {
    viewHolder.bind(item)
  }

  inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(track: Track) {
      itemView.apply {
        trackName.text = track.trackName
        query?.let {
          val startIndex = indexOfSearchQuery(track.trackName)
          if (startIndex != -1) {
            val highLightedName = SpannableString(track.trackName)
            highLightedName.setSpan(
                TextAppearanceSpan(itemView.context, R.style.searchTextHighlight),
                startIndex,
                startIndex + it.length,
                0)
            trackName.text = highLightedName
          }
        }

        trackAlbum.text = track.albumName
        imageRequestManager.load(track.albumCover).into(albumCover)
        setOnClickListener { viewActions.accept(OnTrackClick(track)) }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_AUTO && DateTimeUtil.isNightModeNecessary())) {
          sound.alpha = 0.5f
          albumCover.alpha = 0.5f
        }
      }
    }

    private fun indexOfSearchQuery(trackName: String): Int {
      return if (!TextUtils.isEmpty(query)) {
        trackName.toLowerCase().indexOf(query!!.toLowerCase())
      } else -1
    }
  }
}