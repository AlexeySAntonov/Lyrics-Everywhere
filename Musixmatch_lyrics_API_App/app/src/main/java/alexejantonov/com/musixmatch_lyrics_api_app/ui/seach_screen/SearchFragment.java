package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.R;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseFragment;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.DataAdapter;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType;

public class SearchFragment extends BaseFragment implements SeachScreenContract.View {

	private DataAdapter adapter;
	private RecyclerView recyclerView;
	private SearchPresenter presenter = new SearchPresenter();

	private ProgressBar progressBar;

	public SearchFragment() {
	}

	public static SearchFragment newInstance() {
		return new SearchFragment();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		progressBar = view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

		presenter.onAttach(this);

		activity.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				queryStr = query;
				setToolbarTitle(QueryType.search);
				activity.searchItem.collapseActionView();
				presenter.loadData(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				presenter.loadData(newText);
				return false;
			}
		});
	}

	@Override
	public void onDestroyView() {
		presenter.onDetach();
		super.onDestroyView();
	}

	@Override
	public void showData(List<BaseData> data) {
		progressBar.setVisibility(View.INVISIBLE);
		if (adapter == null) {
			adapter = new DataAdapter(
					data,
					this::launchTrackDetailsActivity,
					this::launchTwitter,
					MyApplication.getImageRequestManager());
			recyclerView.setAdapter(adapter);
		} else {
			adapter.setData(data);
		}
	}
}
