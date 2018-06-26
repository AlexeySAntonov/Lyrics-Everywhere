package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.R
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.DataAdapter
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.DataAdapter.OnTrackClickListener
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.DataAdapter.OnTwitterClickListener
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_search.progressBar
import kotlinx.android.synthetic.main.fragment_search.recyclerView

class SearchFragment : BaseFragment(), SearchFragmentView {
  companion object {
    fun newInstance() = SearchFragment()
  }

  private var adapter: DataAdapter? = null
  private var isSubmitted: Boolean = false

  @InjectPresenter
  lateinit var presenter: SearchPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
      inflater.inflate(R.layout.fragment_search, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    progressBar.visibility = View.VISIBLE

    recyclerView.apply {
      layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
      addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    setToolbarTitle(QueryType.DEFAULT_SEARCH)

    activity.searchView?.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(submitText: String): Boolean {
        isSubmitted = true
        queryTitle = submitText
        setToolbarTitle(QueryType.SEARCH)
        activity.searchItem?.collapseActionView()
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
          imageRequestManager = MyApplication.imageRequestManager,
          query = query
      )
      recyclerView.adapter = adapter
    } else {
      adapter?.updateQueryData(data.toMutableList(), query)
    }
  }
}
