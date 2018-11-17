package com.aleksejantonov.lyricseverywhere.ui.base.delegate

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.LoadingDelegate.LoadingViewHolder
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.entity.LoadingItem
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate

class LoadingDelegate : AbsListItemAdapterDelegate<LoadingItem, ListItem, LoadingViewHolder>() {

  override fun isForViewType(item: ListItem, items: MutableList<ListItem>, position: Int) = item is LoadingItem

  override fun onCreateViewHolder(parent: ViewGroup): LoadingViewHolder {
    return LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))
  }

  override fun onBindViewHolder(item: LoadingItem, viewHolder: LoadingViewHolder, payloads: MutableList<Any>) {
  }

  inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}