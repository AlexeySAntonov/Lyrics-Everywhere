package alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details;

import android.content.SharedPreferences;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class TrackDetailsPresenter extends MvpPresenter<TrackDetailsView> {

	private String trackId;
	private String lyricsText;
	private MusixMatchService musixMatchService = MyApplication.getService();
	private SharedPreferences preferences = MyApplication.getPreferences();
	private CompositeDisposable subscriptions = new CompositeDisposable();

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		loadLyrics();
	}

	void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	private void loadLyrics() {
		subscriptions.add(musixMatchService.getLyrics(preferences.getString(Constants.API_KEY, ""), trackId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						lyricsResponse -> {
							lyricsText = lyricsResponse.getMessage().getBody().getLyrics().getLyricsText();
							getViewState().showData(lyricsText);
						},
						e -> Log.d("Lyrics loading failed", Log.getStackTraceString(e))
				)
		);
	}

	@Override
	public void detachView(TrackDetailsView view) {
		subscriptions.clear();
		super.detachView(view);
	}
}
