package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataContainersUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksScreenContract.Presenter;
import static alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksScreenContract.View;

public class ArtistsAndTracksPresenter implements Presenter {

	private View view;
	private List<Artist> artists = new ArrayList<>();
	private List<Track> tracks = new ArrayList<>();
	private MusixMatchService musixMatchService = MyApplication.getRetrofit().create(MusixMatchService.class);
	private String country;
	private String query;
	private DataBase dataBase = MyApplication.getDataBase();

	@Override
	public void onAttach(View view, String country, String query) {
		this.view = view;
		this.country = country;
		this.query = query;
		loadData();
	}

	@Override
	public void onDetach() {
		view = null;
	}

	@Override
	public void loadData() {
		if (country != null) {
			if (dataBase.getArtists(country).size() > 0 && dataBase.getTracks().size() > 0) {
				//Тащим с БД если не пусто
				Log.d("Loading", country + " top chart artists from Data base");
				listsMerge(dataBase.getArtists(country), dataBase.getTracks());
			} else {
				//Тащим с сервера
				loadArtists();
			}
		} else if (query != null) {
			listsMerge(dataBase.getQueryArtists(query), dataBase.getTracks());
		}
	}

	public void loadArtists() {
		Log.d("Loading", country + " top chart artists from Server");
		musixMatchService.getArtists(country, "1", "100").enqueue(new Callback<ArtistResponse>() {
			@Override
			public void onResponse(Call<ArtistResponse> call, Response<ArtistResponse> response) {
				if (response.isSuccessful()) {
					artists = DataContainersUtils.artistContainersToArtists(
							response.body()
									.getMessage()
									.getBody()
									.getArtistContainers(),
							country
					);

					//Только если загрузились исполнители, начинаем загружать треки
					Log.d("Loading", "Tracks");
					loadTracks();
				}
			}

			@Override
			public void onFailure(Call<ArtistResponse> call, Throwable t) {

			}
		});
	}

	public void loadTracks() {

		musixMatchService.getTracks(country, "1", "100").enqueue(new Callback<TrackResponse>() {
			@Override
			public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
				if (response.isSuccessful()) {
					tracks = DataContainersUtils.trackContainersToTracks(
							response.body()
									.getMessage()
									.getBody()
									.getTrackContainers()
					);
					//Если и треки успешно загрузились, обновляем данные в БД и мержим списки для адаптера
					dataBase.insertArtists(artists);
					dataBase.insertTracks(tracks);
					listsMerge(artists, tracks);
				}
			}

			@Override
			public void onFailure(Call<TrackResponse> call, Throwable t) {

			}
		});
	}

	private void listsMerge(List<Artist> artists, List<Track> tracks) {
		List<BaseData> data = new ArrayList<>();

		for (int i = 0; i < artists.size(); i++) {
			data.add(artists.get(i));

			for (int j = 0; j < tracks.size(); j++) {
				if (artists.get(i).getArtistId() == tracks.get(j).getArtistId()) {
					data.add(tracks.get(j));
				}
			}
		}
		if (view != null) {
			view.showData(data);
		}
	}
}
