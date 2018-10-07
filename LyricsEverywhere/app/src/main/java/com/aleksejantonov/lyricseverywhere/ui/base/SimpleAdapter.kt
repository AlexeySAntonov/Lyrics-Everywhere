package com.aleksejantonov.lyricseverywhere.ui.base

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter

open class SimpleAdapter  : ListDelegationAdapter<List<ListItem>>() {

  override fun setItems(items: List<ListItem>?) {
    super.setItems(items)
    notifyDataSetChanged()
  }

  fun updateItem(item: ListItem, index: Int) {
    super.setItems(items.toMutableList().apply { set(index, item) })
    notifyItemChanged(index)
  }
}