package com.aleksejantonov.lyricseverywhere.ui.base.delegate

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.PlaceHolderDelegate.PlaceHolderViewHolder
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.entity.PlaceHolderItem
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_place_holder.view.placeHolderImage

class PlaceHolderDelegate : AbsListItemAdapterDelegate<PlaceHolderItem, ListItem, PlaceHolderViewHolder>() {

  override fun isForViewType(item: ListItem, items: MutableList<ListItem>, position: Int) = item is PlaceHolderItem

  override fun onCreateViewHolder(parent: ViewGroup): PlaceHolderViewHolder {
    return PlaceHolderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place_holder, parent, false))
  }

  override fun onBindViewHolder(item: PlaceHolderItem, viewHolder: PlaceHolderViewHolder, payloads: MutableList<Any>) {
    viewHolder.bind(item)
  }

  inner class PlaceHolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: PlaceHolderItem) {
      itemView.apply { placeHolderImage.setImageResource(item.imagerRes) }
    }
  }
}