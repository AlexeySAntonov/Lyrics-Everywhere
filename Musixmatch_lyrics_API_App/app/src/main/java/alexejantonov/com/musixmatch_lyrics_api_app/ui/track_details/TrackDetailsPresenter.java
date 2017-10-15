package alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details;

import android.util.Log;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics.LyricsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsScreenContract.Presenter;
import static alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details.TrackDetailsScreenContract.View;

public class TrackDetailsPresenter implements Presenter {

	private View view;
	private String trackId;
	private String lyricsText;
	private MusixMatchService musixMatchService = MyApplication.getService();

	@Override
	public void onAttach(View view) {
		this.view = view;
		loadLyrics();
	}

	@Override
	public void onDetach() {
		view = null;
	}

	@Override
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	@Override
	public void loadLyrics() {
		musixMatchService.getLyrics(Constants.API_KEY_VALUE, trackId).enqueue(new Callback<LyricsResponse>() {
			@Override
			public void onResponse(Call<LyricsResponse> call, Response<LyricsResponse> response) {
				if (response.isSuccessful()) {
					lyricsText = response.body().getMessage().getBody().getLyrics().getLyricsText();
					view.showData(lyricsText);
				}
			}

			@Override
			public void onFailure(Call<LyricsResponse> call, Throwable t) {
				Log.d("Lyrics loading failed", t.getMessage());
			}
		});
	}
}
