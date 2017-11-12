package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.content.SharedPreferences;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataContainersUtil;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class ArtistsAndTracksPresenter extends MvpPresenter<ArtistsAndTracksListView> {

	private List<Artist> artists = new ArrayList<>();
	private List<Track> tracks = new ArrayList<>();
	private MusixMatchService musixMatchService = MyApplication.getService();
	private String country;
	private DataBase dataBase = MyApplication.getDataBase();
	private CompositeDisposable subscriptions = new CompositeDisposable();
	private SharedPreferences preferences = MyApplication.getPreferences();

	@Override
	protected void onFirstViewAttach() {
		super.onFirstViewAttach();
		loadData();
	}

	void setCountry(String country) {
		this.country = country;
	}

	void loadData() {
		if (country == null) {
			country = QueryType.ru.name();
		}
		if (dataBase.getCountryArtists(country).size() > 0 && dataBase.getTracks().size() > 0) {
			//Тащим с БД если не пусто
			getViewState().showData(DataMergeUtil.listsMerge(dataBase.getCountryArtists(country), dataBase.getTracks()));
		} else {
			loadArtists();
		}
	}

	void loadArtists() {
		Log.d("Loading", country + " top chart artists from Server");
		subscriptions.add(musixMatchService.getArtists(preferences.getString(Constants.API_KEY, ""), country, "1", "100")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						response -> {
							artists = DataContainersUtil.artistContainersToArtists(
									response.getMessage().getBody().getArtistContainers(), country
							);
							//Только если загрузились исполнители, начинаем загружать треки
							Log.d("Loading", "Tracks");
							loadTracks();
						},
						e -> Log.d("Artists loading failed", Log.getStackTraceString(e))
				)
		);
	}

	private void loadTracks() {
		subscriptions.add(musixMatchService.getTracks(preferences.getString(Constants.API_KEY, ""), country, "1", "100")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(
						trackResponse -> {
							tracks = DataContainersUtil.trackContainersToTracks(
									trackResponse.getMessage().getBody().getTrackContainers()
							);
							dataBase.insertArtists(artists);
							dataBase.insertTracks(tracks);
							getViewState().showData(DataMergeUtil.listsMerge(artists, tracks));
						},
						e -> Log.d("Tracks loading failed", Log.getStackTraceString(e))
				)
		);
	}

	@Override
	public void detachView(ArtistsAndTracksListView view) {
		subscriptions.clear();
		super.detachView(view);
	}
}
