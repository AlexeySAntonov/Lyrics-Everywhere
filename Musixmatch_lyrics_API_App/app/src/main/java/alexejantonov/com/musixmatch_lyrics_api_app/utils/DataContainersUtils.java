package alexejantonov.com.musixmatch_lyrics_api_app.utils;

import java.util.ArrayList;
import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistContainer;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackContainer;

public class DataContainersUtils {

	public DataContainersUtils() {
	}

	public static List<Artist> artistContainersToArtists(List<ArtistContainer> artistContainers, String country) {
		List<Artist> artists = new ArrayList<>();
		for (int i = 0; i < artistContainers.size(); i++) {
			artists.add(artistContainers.get(i).getArtist());
		}

		for (Artist artist : artists) {
			artist.setTopChartCountries(artist.getTopChartCountries() + country);
		}

		return artists;
	}

	public static List<Track> trackContainersToTracks(List<TrackContainer> trackContainers) {
		List<Track> tracks = new ArrayList<>();
		for (int i = 0; i < trackContainers.size(); i++) {
			tracks.add(trackContainers.get(i).getTrack());
		}

		return tracks;
	}
}
