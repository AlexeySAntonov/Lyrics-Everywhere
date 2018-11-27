package com.aleksejantonov.lyricseverywhere.ui.artistsandtracks

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.sl.SL
import com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate.ArtistItemDelegate
import com.aleksejantonov.lyricseverywhere.ui.artistsandtracks.delegate.TrackItemDelegate
import com.aleksejantonov.lyricseverywhere.ui.base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.base.ListItem
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType.RU
import com.aleksejantonov.lyricseverywhere.ui.base.ScreenType.SEARCH
import com.aleksejantonov.lyricseverywhere.ui.base.SimpleAdapter
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.LoadingDelegate
import com.aleksejantonov.lyricseverywhere.utils.NetworkUtil
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_artists.recyclerView
import kotlinx.android.synthetic.main.fragment_artists.searchOverlay
import kotlinx.android.synthetic.main.fragment_artists.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_artists.toolbar

class ArtistsAndTracksListFragment : BaseFragment(), ArtistsAndTracksListView {

  companion object {
    private val BUNDLE_QUERY_TYPE = ArtistsAndTracksListFragment::class.java.simpleName + ".query"

    fun newInstance(queryType: QueryType) = ArtistsAndTracksListFragment().apply {
      arguments = Bundle().apply { putSerializable(BUNDLE_QUERY_TYPE, queryType) }
    }
  }

  private lateinit var queryType: QueryType
  private val adapter by lazy { ArtistsAndTracksAdapter() }

  @InjectPresenter
  lateinit var presenter: ArtistsAndTracksPresenter

  override val layoutRes = R.layout.fragment_artists

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    queryType = arguments?.getSerializable(BUNDLE_QUERY_TYPE) as QueryType? ?: RU

    searchOverlay.setOnClickListener { activity.navigateTo(screen = SEARCH, addToBackStack = true, animate = true) }

    toolbar.apply {
      title = when (queryType) {
        QueryType.US -> getText(R.string.usa_top_chart)
        QueryType.GB -> getText(R.string.britain_top_chart)
        else         -> getText(R.string.russian_top_chart)
      }
      setNavigationOnClickListener { activity.toggleDrawer() }
    }
    presenter.setCountry(queryType.name)

    context?.let {
      swipeRefreshLayout.setOnRefreshListener {
        if (NetworkUtil.isOnline(it)) presenter.loadArtists()
        else showLostInternetConnectionDialog()
        swipeRefreshLayout.isRefreshing = false
      }
      recyclerView.apply {
        layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
        adapter = this@ArtistsAndTracksListFragment.adapter
      }
    }
  }

  override fun showItems(items: List<ListItem>) {
    swipeRefreshLayout.isRefreshing = false
    adapter.items = items
  }

  override fun showTwitter(twitterUrl: String) = launchTwitter(twitterUrl)

  override fun showTrackDetails(track: Track, sharedImageView: ImageView) = launchTrackDetailsActivity(track, sharedImageView)

  private fun showLostInternetConnectionDialog() {
    context?.let {
      AlertDialog.Builder(it, R.style.Dialog)
          .setTitle(R.string.internet_connection_problems)
          .setNeutralButton(R.string.load_from_database) { _, _ ->
            presenter.apply {
              setCountry(queryType.name)
              loadData()
            }
          }
          .setPositiveButton(R.string.retry) { _, _ ->
            if (NetworkUtil.isOnline(it)) {
              presenter.apply {
                setCountry(queryType.name)
                presenter.loadData()
              }
            } else {
              showLostInternetConnectionDialog()
            }
          }
          .setOnDismissListener { swipeRefreshLayout.isRefreshing = false }
          .create()
          .show()
    }
  }

  private inner class ArtistsAndTracksAdapter : SimpleAdapter() {
    init {
      delegatesManager.apply {
        addDelegate(LoadingDelegate())
        addDelegate(ArtistItemDelegate(viewActions = presenter.viewActions))
        addDelegate(TrackItemDelegate(viewActions = presenter.viewActions, imageRequestManager = SL.componentManager().appComponent.imageRequestManager))
      }
    }
  }
}
