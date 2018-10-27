package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate

import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.R.style
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTwitterClick
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.utils.DateTimeUtil
import com.aleksejantonov.lyricseverywhere.utils.highlight
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.jakewharton.rxrelay2.PublishRelay
import kotlinx.android.synthetic.main.item_artist.view.artistName
import kotlinx.android.synthetic.main.item_artist.view.divider
import kotlinx.android.synthetic.main.item_artist.view.twitterIcon

class ArtistItemDelegate(
    private var query: String? = null,
    private val viewActions: PublishRelay<Any>
) : AbsListItemAdapterDelegate<Artist, ListItem, ArtistItemDelegate.ArtistViewHolder>() {

  override fun isForViewType(item: ListItem, items: List<ListItem>, position: Int) = item is Artist

  override fun onCreateViewHolder(parent: ViewGroup) =
      ArtistViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false))

  override fun onBindViewHolder(item: Artist, viewHolder: ArtistViewHolder, payloads: List<Any>) {
    viewHolder.bind(item)
  }

  fun setQuery(query: String) {
    this.query = query
  }

  inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(artist: Artist) {
      itemView.apply {
        artistName.text = artist.artistName
        query?.let { artist.artistName.highlight(it, context)?.let { artistName.text = it } }

        twitterIcon.setOnClickListener { viewActions.accept(OnTwitterClick(artist.twitterUrl)) }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_AUTO && DateTimeUtil.isNightModeNecessary())) {
          ContextCompat.getColor(context, R.color.defaultGrayColor).let {
            divider.setBackgroundColor(it)
            artistName.setTextColor(it)
          }
          twitterIcon.alpha = 0.5f
        }
      }
    }
  }
}