package com.aleksejantonov.lyricseverywhere.ui.artists_and_tracks

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aleksejantonov.lyricseverywhere.MyApplication
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.Base.BaseData
import com.aleksejantonov.lyricseverywhere.ui.Base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.Base.DataAdapter
import com.aleksejantonov.lyricseverywhere.ui.Base.DataAdapter.OnTrackClickListener
import com.aleksejantonov.lyricseverywhere.ui.Base.DataAdapter.OnTwitterClickListener
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType.RU
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType.SEARCH
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_artists.progressBar
import kotlinx.android.synthetic.main.fragment_artists.recyclerView
import kotlinx.android.synthetic.main.fragment_artists.searchIcon
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
  private var adapter: DataAdapter? = null

  @InjectPresenter
  lateinit var presenter: ArtistsAndTracksPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_artists, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    queryType = arguments?.getSerializable(BUNDLE_QUERY_TYPE) as QueryType? ?: RU

    searchIcon.setOnClickListener { activity.navigateTo(SEARCH, QueryType.SEARCH, true) }

    toolbar.apply {
      title = when (queryType) {
        QueryType.US -> getText(R.string.usa_top_chart)
        QueryType.GB -> getText(R.string.britain_top_chart)
        else         -> getText(R.string.russian_top_chart)
      }
      setNavigationOnClickListener { activity.setDrawerState() }
    }
    presenter.setCountry(queryType.name)

    context?.let {
      swipeRefreshLayout.setOnRefreshListener {
        if (MyApplication.isOnline(it)) presenter.loadArtists()
        else showLostInternetConnectionDialog()
        swipeRefreshLayout.isRefreshing = false
      }
      recyclerView.apply {
        layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
      }
    }
  }

  override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
  }

  override fun showData(data: List<BaseData>) {
    progressBar.visibility = View.INVISIBLE
    swipeRefreshLayout.isRefreshing = false

    context?.let {
      if (adapter == null) {
        adapter = DataAdapter(
            data = data.toMutableList(),
            onTrackClickListener = object : OnTrackClickListener {
              override fun onClick(track: Track) {
                launchTrackDetailsActivity(track)
              }
            },
            onTwitterClickListener = object : OnTwitterClickListener {
              override fun onClick(twitterUrl: String) {
                launchTwitter(twitterUrl)
              }
            },
            imageRequestManager = MyApplication.imageRequestManager,
            query = null
        )
        recyclerView.adapter = adapter
      } else {
        adapter?.updateData(data)
      }
    }
  }

  override fun showLoading() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progressBar.visibility = View.GONE
  }

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
            if (MyApplication.isOnline(it)) {
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
}
