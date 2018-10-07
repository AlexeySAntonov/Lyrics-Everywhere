package com.aleksejantonov.lyricseverywhere.ui.search

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.MenuItem
import android.view.View
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.base.BaseData
import com.aleksejantonov.lyricseverywhere.ui.base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.base.DataAdapter
import com.aleksejantonov.lyricseverywhere.ui.base.DataAdapter.OnTrackClickListener
import com.aleksejantonov.lyricseverywhere.ui.base.DataAdapter.OnTwitterClickListener
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_search.progressBar
import kotlinx.android.synthetic.main.fragment_search.recyclerView
import kotlinx.android.synthetic.main.fragment_search.toolbar

class SearchFragment : BaseFragment(), SearchFragmentView {
  companion object {
    fun newInstance() = SearchFragment()
  }

  private lateinit var searchView: SearchView
  private lateinit var searchItem: MenuItem
  private var adapter: DataAdapter? = null
  private var isSubmitted: Boolean = false
  private var queryTitle: String? = null

  @InjectPresenter
  lateinit var presenter: SearchPresenter

  override val layoutRes = R.layout.fragment_search

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    recyclerView.apply {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    toolbar.apply {
      title = getText(R.string.search_by_artist_name)
      setNavigationOnClickListener { activity.onBackPressed() }
      inflateMenu(R.menu.search_menu)
    }
    setupSearchView()
  }

  override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
  }

  override fun showData(data: List<BaseData>, query: String) {
    progressBar.visibility = View.INVISIBLE
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
          imageRequestManager = DI.componentManager().appComponent.imageRequestManager,
          query = query
      )
      recyclerView.adapter = adapter
    } else {
      adapter?.updateQueryData(data, query)
    }
  }

  override fun showLoading() {
    progressBar.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    progressBar.visibility = View.GONE
  }

  private fun setupSearchView() {
    searchItem = toolbar.menu.findItem(R.id.search)
    searchItem.expandActionView()
    searchView = searchItem.actionView as SearchView
    searchView.apply {
      queryHint = getString(R.string.search_by_artist_name)
      isSubmitButtonEnabled = true
      setIconifiedByDefault(false)
    }

    searchView.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(submitText: String): Boolean {
        isSubmitted = true
        queryTitle = submitText
        toolbar.title = String.format(getString(R.string.query_results), queryTitle)
        searchItem.collapseActionView()
        presenter.loadData(submitText)
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        if (isSubmitted.not()) {
          queryTitle = newText
          presenter.loadData(newText)
        } else {
          isSubmitted = false
        }
        return false
      }
    })
  }
}
