package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.RequestManager;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.MainActivity;
import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.R;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsActivity;

public class ArtistsAndTracksListFragment extends Fragment implements ArtistsAndTracksScreenContract.View {

	private static final String BUNDLE_COUNTRY = ArtistsAndTracksListFragment.class.getSimpleName() + ".country";
	private static final String BUNDLE_QUERY = ArtistsAndTracksListFragment.class.getSimpleName() + " .query";

	private ArtistsAndTracksPresenter presenter = new ArtistsAndTracksPresenter();
	private String country;
	private String query;
	private RequestManager imageRequestManager;

	private RecyclerView recyclerView;
	private ProgressBar progressBar;
	private SwipeRefreshLayout swipeRefreshLayout;

	public ArtistsAndTracksListFragment() {
	}

	public static ArtistsAndTracksListFragment newInstance(String country) {
		Bundle args = new Bundle();
		args.putString(BUNDLE_COUNTRY, country);
		ArtistsAndTracksListFragment artistsAndTracksListFragment = new ArtistsAndTracksListFragment();
		artistsAndTracksListFragment.setArguments(args);
		return artistsAndTracksListFragment;
	}

	public static ArtistsAndTracksListFragment newInstance(String country, String query) {
		Bundle args = new Bundle();
		args.putString(BUNDLE_COUNTRY, country);
		args.putString(BUNDLE_QUERY, query);
		ArtistsAndTracksListFragment artistsAndTracksListFragment = new ArtistsAndTracksListFragment();
		artistsAndTracksListFragment.setArguments(args);
		return artistsAndTracksListFragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return LayoutInflater.from(getContext()).inflate(R.layout.fragment_artists, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		progressBar = view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);

		country = getArguments().getString(BUNDLE_COUNTRY);
		query = getArguments().getString(BUNDLE_QUERY);

		setToolbarTitle(country, query);

		swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(() -> {
			if (MyApplication.isOnline(getContext()) && query == null) {
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

		if (country != null) {
			Log.d("Presenter", "onAttach()");
			presenter.onAttach(this, country, null);
		} else {
			Log.d("Performed query: ", query);
			presenter.onAttach(this, null, query);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("Presenter", "onDetach()");
		presenter.onDetach();
	}

	@Override
	public void showData(List<BaseData> data) {
		progressBar.setVisibility(View.INVISIBLE);
		swipeRefreshLayout.setRefreshing(false);

		if (getContext() != null) {
			imageRequestManager = MyApplication.getImageRequestManager();
			recyclerView.setAdapter(new DataAdapter(
					data,
					this::launchTrackDetailsActivity,
					this::launchTwitter,
					imageRequestManager)
			);
		}
	}

	private void launchTrackDetailsActivity(Track track) {
		Intent i = TrackDetailsActivity.newIntent(getContext(), track);
		startActivity(i);
	}

	private void launchTwitter(String twitterUrl) {
		if (twitterUrl.length() > 0) {
			Log.d("twitter", "twitter://user?screen_name=" + twitterUrl.substring(20));
			try {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterUrl.substring(20))));
			} catch (Exception e) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)));
			}
		} else {
			Snackbar.make(getView(), R.string.no_twitter, Snackbar.LENGTH_LONG).show();
		}
	}

	private void showLostInternetConnectionDialog() {
		new AlertDialog.Builder(getContext(), R.style.Dialog)
				.setTitle(R.string.internet_connection_problems)
				.setNeutralButton(R.string.load_from_database, (dialogInterface, i) -> presenter.onAttach(ArtistsAndTracksListFragment.this, country, null))
				.setPositiveButton(R.string.retry, (dialogInterface, i) -> {
					if (MyApplication.isOnline(getContext())) {
						presenter.onAttach(ArtistsAndTracksListFragment.this, country, null);
					} else {
						showLostInternetConnectionDialog();
					}
				})
				.setOnDismissListener(dialogInterface -> swipeRefreshLayout.setRefreshing(false))
				.create().show();
	}

	private void setToolbarTitle(String country, String query) {
		ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();

		if (country != null) {
			switch (country) {
				case "ru":
					actionBar.setTitle(R.string.russian_top_chart);
					break;
				case "us":
					actionBar.setTitle(R.string.usa_top_chart);
					break;
				case "gb":
					actionBar.setTitle(R.string.britain_top_chart);
					break;
			}
		} else {
			actionBar.setTitle(getString(R.string.query_results) + query + "\"");
		}
	}
}
