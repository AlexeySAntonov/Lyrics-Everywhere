package alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics.LyricsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class TrackDetailsPresenter extends MvpPresenter<TrackDetailsView> {

	private String trackId;
	private String lyricsText;
	private MusixMatchService musixMatchService = MyApplication.getService();

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		loadLyrics();
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public void loadLyrics() {
		musixMatchService.getLyrics(Constants.API_KEY_VALUE, trackId).enqueue(new Callback<LyricsResponse>() {
			@Override
			public void onResponse(Call<LyricsResponse> call, Response<LyricsResponse> response) {
				if (response.isSuccessful()) {
					lyricsText = response.body().getMessage().getBody().getLyrics().getLyricsText();
					getViewState().showData(lyricsText);
				}
			}

			@Override
			public void onFailure(Call<LyricsResponse> call, Throwable t) {
				Log.d("Lyrics loading failed", t.getMessage());
			}
		});
	}
}
