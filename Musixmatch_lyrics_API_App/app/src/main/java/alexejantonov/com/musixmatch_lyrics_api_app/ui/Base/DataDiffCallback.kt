package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base

import android.support.v7.util.DiffUtil

class DataDiffCallback(private val oldList: List<BaseData>, private val newList: List<BaseData>) : DiffUtil.Callback() {

  override fun getOldListSize() = oldList.size

  override fun getNewListSize() = newList.size

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition].artistId == newList[newItemPosition].artistId
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition] == newList[newItemPosition]
  }
}
