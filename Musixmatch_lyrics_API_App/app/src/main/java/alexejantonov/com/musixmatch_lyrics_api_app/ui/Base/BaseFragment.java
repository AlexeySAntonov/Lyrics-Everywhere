package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base;

import alexejantonov.com.musixmatch_lyrics_api_app.MainActivity;
import alexejantonov.com.musixmatch_lyrics_api_app.R;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksListFragment;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen.SearchFragment;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import com.arellomobile.mvp.MvpAppCompatFragment;

public abstract class BaseFragment extends MvpAppCompatFragment {

	public MainActivity activity;
	public String queryTitle;

	public static BaseFragment newInstance(FragmentType type, QueryType query) {
		switch (type) {
			case COUNTRY:
				return ArtistsAndTracksListFragment.newInstance(query);
			case SEARCH:
				return SearchFragment.Companion.newInstance();
		}
		throw new IllegalArgumentException("Unknown fragment type: " + type.toString());
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainActivity) getActivity();
	}

	public void launchTrackDetailsActivity(Track track) {
		Intent i = TrackDetailsActivity.Companion.newIntent(getContext(), track);
		startActivity(i);
	}

	public void launchTwitter(String twitterUrl) {
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

	public void setToolbarTitle(QueryType query) {
		ActionBar actionBar = activity.getSupportActionBar();
		switch (query) {
			case RU:
				actionBar.setTitle(R.string.russian_top_chart);
				break;
			case US:
				actionBar.setTitle(R.string.usa_top_chart);
				break;
			case GB:
				actionBar.setTitle(R.string.britain_top_chart);
				break;
			case SEARCH:
				actionBar.setTitle("Results for query \"" + queryTitle + " \"");
				break;
			case DEFAULT_SEARCH:
				actionBar.setTitle("Do a Search");
		}
	}
}
