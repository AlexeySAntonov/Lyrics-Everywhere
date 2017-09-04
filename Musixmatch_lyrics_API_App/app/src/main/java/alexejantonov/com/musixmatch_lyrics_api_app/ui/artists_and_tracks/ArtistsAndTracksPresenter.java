package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistContainer;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackContainer;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataContainersUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksScreenContract.*;

public class ArtistsAndTracksPresenter implements Presenter {

	private View view;
	private List<ArtistContainer> artistContainers = new ArrayList<>();
	private List<TrackContainer> trackContainers = new ArrayList<>();
	private MusixMatchService musixMatchService;
	private String country;
	private DataBase dataBase;

	@Override
	public void onAttach(DataBase dataBase, MusixMatchService musixMatchService, View view, String country) {
		this.dataBase = dataBase;
		this.musixMatchService = musixMatchService;
		this.view = view;
		this.country = country;
		loadArtists();
	}

	@Override
	public void onDetach() {
		view = null;
	}

	@Override
	public void loadArtists() {
		musixMatchService.getArtists(country, "1", "100").enqueue(new Callback<ArtistResponse>() {
			@Override
			public void onResponse(Call<ArtistResponse> call, Response<ArtistResponse> response) {
				if (response.isSuccessful()) {
					artistContainers = response.body().getMessage().getBody().getArtistContainers();

					loadTracks();
					Log.d("Loading", "Tracks");
				}
			}

			@Override
			public void onFailure(Call<ArtistResponse> call, Throwable t) {

			}
		});
	}

	@Override
	public void loadTracks() {
		musixMatchService.getTracks(country, "1", "100").enqueue(new Callback<TrackResponse>() {
			@Override
			public void onResponse(Call<TrackResponse> call, Response<TrackResponse> response) {
				if (response.isSuccessful()) {
					trackContainers = response.body().getMessage().getBody().getTrackContainers();

					listsMerge(artistContainers, trackContainers);
				}
			}

			@Override
			public void onFailure(Call<TrackResponse> call, Throwable t) {

			}
		});
	}

	private void listsMerge(List<ArtistContainer> artistContainers, List<TrackContainer> trackContainers) {

		List<Artist> artists = new ArrayList<>(DataContainersUtils.artistContainersToArtists(artistContainers));
		dataBase.insertArtists(artists);

		List<Track> tracks = new ArrayList<>(DataContainersUtils.trackContainersToTracks(trackContainers));
		dataBase.insertTracks(tracks);

		List<BaseData> data = new ArrayList<>();

		for (int i = 0; i < artists.size(); i++) {
			data.add(artists.get(i));

			for (int j = 0; j < tracks.size(); j++) {
				if (artists.get(i).getArtistId() == tracks.get(j).getArtistId()) {
					data.add(tracks.get(j));
				}
			}
		}

		view.showData(data);
	}
}
