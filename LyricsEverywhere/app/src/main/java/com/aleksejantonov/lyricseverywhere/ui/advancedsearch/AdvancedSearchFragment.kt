package com.aleksejantonov.lyricseverywhere.ui.advancedsearch

import android.os.Bundle
import android.view.View
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.ui.base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.ui.base.SimpleAdapter
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.LoadingDelegate
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.PlaceHolderDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_advanced_search.navigationOverlay
import kotlinx.android.synthetic.main.fragment_advanced_search.recyclerView

class AdvancedSearchFragment : BaseFragment(), AdvancedSearchView {
  companion object {
    fun newInstance() = AdvancedSearchFragment()
  }

  private val adapter by lazy { AdvancedSearchAdapter() }

  @InjectPresenter
  lateinit var presenter: AdvancedSearchPresenter

  override val layoutRes = R.layout.fragment_advanced_search

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    navigationOverlay.setOnClickListener { activity.toggleDrawer() }

    with(recyclerView) {
      adapter = this@AdvancedSearchFragment.adapter
    }
  }

  override fun showItems(items: List<ListItem>) {
    adapter.items = items
  }

  private inner class AdvancedSearchAdapter : SimpleAdapter() {
    init {
      delegatesManager.apply {
        addDelegate(LoadingDelegate())
        addDelegate(PlaceHolderDelegate())
      }
    }
  }
}