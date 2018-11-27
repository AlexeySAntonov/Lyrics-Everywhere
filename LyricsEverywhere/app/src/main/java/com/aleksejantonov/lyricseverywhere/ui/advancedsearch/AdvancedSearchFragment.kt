package com.aleksejantonov.lyricseverywhere.ui.advancedsearch

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.sl.SL
import com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate.TrackItemDelegate
import com.aleksejantonov.lyricseverywhere.ui.base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnSearchClick
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.ui.base.SimpleAdapter
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.LoadingDelegate
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.PlaceHolderDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_advanced_search.navigationOverlay
import kotlinx.android.synthetic.main.fragment_advanced_search.recyclerView
import kotlinx.android.synthetic.main.fragment_advanced_search.searchButton
import kotlinx.android.synthetic.main.fragment_advanced_search.searchEditText

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
    searchButton.setOnClickListener { presenter.viewActions.accept(OnSearchClick(searchEditText.text.toString())) }
  }

  override fun showItems(items: List<ListItem>) {
    adapter.items = items
  }

  override fun showTwitter(twitterUrl: String) = Unit

  override fun showTrackDetails(track: Track, sharedImageView: ImageView) = launchTrackDetailsActivity(track, sharedImageView)

  private inner class AdvancedSearchAdapter : SimpleAdapter() {
    init {
      delegatesManager.apply {
        addDelegate(LoadingDelegate())
        addDelegate(PlaceHolderDelegate())
        addDelegate(TrackItemDelegate(viewActions = presenter.viewActions, imageRequestManager = SL.componentManager().appComponent.imageRequestManager))
      }
    }
  }
}