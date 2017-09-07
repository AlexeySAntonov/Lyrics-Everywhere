package alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import alexejantonov.com.musixmatch_lyrics_api_app.R;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;

import static alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsScreenContract.View;

public class TrackDetailsActivity extends AppCompatActivity implements View {

	private static final String EXTRA_TRACK = TrackDetailsActivity.class.getSimpleName() + "track";

	private Track track;
	private RequestManager imageRequestManager;
	private TrackDetailsPresenter presenter = new TrackDetailsPresenter();

	private TextView trackName;
	private TextView trackAlbum;
	private ImageView albumCover;
	private TextView lyrics;
	private Toolbar toolbar;

	public static Intent newIntent(Context packageContext, Track track) {
		Intent i = new Intent(packageContext, TrackDetailsActivity.class);
		i.putExtra(EXTRA_TRACK, track);
		return i;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track_details);

		track = (Track) getIntent().getSerializableExtra(EXTRA_TRACK);

		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(getString(R.string.lyrics_of) + "\"" + track.getTrackName() + "\"");
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
		toolbar.setNavigationOnClickListener(v -> onBackPressed());

		trackName = findViewById(R.id.trackName);
		trackAlbum = findViewById(R.id.trackAlbum);
		albumCover = findViewById(R.id.albumCover);
		lyrics = findViewById(R.id.lyrics);

		trackName.setText("\"" + track.getTrackName() + "\"");
		trackAlbum.setText(getString(R.string.album) + " \"" + track.getAlbumName() + "\"");
		imageRequestManager = Glide.with(this);
		imageRequestManager.load(track.getAlbumCover()).into(albumCover);

		presenter.setTrackId(String.valueOf(track.getTrackId()));
		presenter.onAttach(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		presenter.onDetach();
	}

	@Override
	public void showData(String lyricsText) {
		lyrics.setText(lyricsText);
	}
}
