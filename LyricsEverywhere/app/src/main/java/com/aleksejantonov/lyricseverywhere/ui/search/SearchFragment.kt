package com.aleksejantonov.lyricseverywhere.ui.search

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate.ArtistItemDelegate
import com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate.TrackItemDelegate
import com.aleksejantonov.lyricseverywhere.ui.base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.ui.base.SimpleAdapter
import com.aleksejantonov.lyricseverywhere.utils.getColor
import com.aleksejantonov.lyricseverywhere.utils.hideKeyboard
import com.aleksejantonov.lyricseverywhere.utils.setTextColor
import com.aleksejantonov.lyricseverywhere.utils.setUnderscoreColor
import com.aleksejantonov.lyricseverywhere.utils.textChangeListener
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_search.navigationOverlay
import kotlinx.android.synthetic.main.fragment_search.progressBar
import kotlinx.android.synthetic.main.fragment_search.recyclerView
import kotlinx.android.synthetic.main.fragment_search.searchView

class SearchFragment : BaseFragment(), SearchFragmentView {
  companion object {
    fun newInstance() = SearchFragment()
  }

  private val adapter by lazy { ArtistsAndTracksAdapter() }

  @InjectPresenter
  lateinit var presenter: SearchPresenter

  private val artistDelegate by lazy { ArtistItemDelegate(viewActions = presenter.viewActions) }
  private val trackDelegate by lazy { TrackItemDelegate(viewActions = presenter.viewActions, imageRequestManager = DI.componentManager().appComponent.imageRequestManager) }

  override val layoutRes = R.layout.fragment_search

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    recyclerView.apply {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
      adapter = this@SearchFragment.adapter
    }

    navigationOverlay.setOnClickListener {
      searchView.hideKeyboard()
      activity.onBackPressed()
    }
    setupSearchView()
  }

  override fun showData(items: List<ListItem>, query: String) {
    progressBar.visibility = View.INVISIBLE
    adapter.items = items
  }

  override fun setQuery(query: String) {
    artistDelegate.setQuery(query)
    trackDelegate.setQuery(query)
  }

  override fun showLoading() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progressBar.visibility = View.GONE
  }

  override fun showTwitter(twitterUrl: String) = launchTwitter(twitterUrl)

  override fun showTrackDetails(track: Track, sharedImageView: ImageView) = launchTrackDetailsActivity(track, sharedImageView)

  private fun setupSearchView() {
    with(searchView) {
      isIconified = false
      queryHint = Html.fromHtml(getString(R.string.search_hint_text))
      setTextColor(getColor(R.color.white))
      setUnderscoreColor(getColor(R.color.white))
      textChangeListener(presenter::loadData)
    }
  }

  private inner class ArtistsAndTracksAdapter : SimpleAdapter() {
    init {
      delegatesManager.apply {
        addDelegate(artistDelegate)
        addDelegate(trackDelegate)
      }
    }
  }
}
