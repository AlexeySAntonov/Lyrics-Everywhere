package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ArtistsAndTracksListFragment extends BaseFragment implements ArtistsAndTracksScreenContract.View {

	private static final String BUNDLE_COUNTRY = ArtistsAndTracksListFragment.class.getSimpleName() + " .country";

	private ArtistsAndTracksPresenter presenter = new ArtistsAndTracksPresenter();
	private QueryType country;
	private DataAdapter adapter;

	private RecyclerView recyclerView;
	private ProgressBar progressBar;
	private SwipeRefreshLayout swipeRefreshLayout;

	public ArtistsAndTracksListFragment() {
	}

	public static ArtistsAndTracksListFragment newInstance(QueryType country) {
		Bundle args = new Bundle();
		args.putSerializable(BUNDLE_COUNTRY, country);
		ArtistsAndTracksListFragment artistsAndTracksListFragment = new ArtistsAndTracksListFragment();
		artistsAndTracksListFragment.setArguments(args);
		return artistsAndTracksListFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_artists, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		progressBar = view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		country = (QueryType) getArguments().getSerializable(BUNDLE_COUNTRY);

		setToolbarTitle(country);

		swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(() -> {
			if (MyApplication.isOnline(getContext())) {
				presenter.loadArtists();
			} else if (!MyApplication.isOnline(getContext())) {
				showLostInternetConnectionDialog();
			} else {
				swipeRefreshLayout.setRefreshing(false);
			}
		});

		recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

		presenter.onAttach(this, country.name());
	}

	@Override
	public void onDestroyView() {
		adapter = null;
		presenter.onDetach();
		super.onDestroyView();
	}

	@Override
	public void showData(List<BaseData> data) {
		progressBar.setVisibility(View.INVISIBLE);
		swipeRefreshLayout.setRefreshing(false);

		if (getContext() != null) {

			if (adapter == null) {
				adapter = new DataAdapter(
						data,
						this::launchTrackDetailsActivity,
						this::launchTwitter,
						MyApplication.getImageRequestManager(),
						null);
				recyclerView.setAdapter(adapter);
			} else {
				adapter.updateData(data);
			}
		}
	}

	private void showLostInternetConnectionDialog() {
		new AlertDialog.Builder(getContext(), R.style.Dialog)
				.setTitle(R.string.internet_connection_problems)
				.setNeutralButton(R.string.load_from_database, (dialogInterface, i) -> presenter.onAttach(ArtistsAndTracksListFragment.this, country.name()))
				.setPositiveButton(R.string.retry, (dialogInterface, i) -> {
					if (MyApplication.isOnline(getContext())) {
						presenter.onAttach(ArtistsAndTracksListFragment.this, country.name());
					} else {
						showLostInternetConnectionDialog();
					}
				})
				.setOnDismissListener(dialogInterface -> swipeRefreshLayout.setRefreshing(false))
				.create().show();
	}
}
