package alexejantonov.com.musixmatch_lyrics_api_app.utils;

import java.util.ArrayList;
import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData;

public class DataMergeUtil {

	public DataMergeUtil() {
	}

	public static List<BaseData> listsMerge(List<Artist> artists, List<Track> tracks) {
		List<BaseData> data = new ArrayList<>();

		for (int i = 0; i < artists.size(); i++) {
			data.add(artists.get(i));
			for (int j = 0; j < tracks.size(); j++) {
				if (artists.get(i).getArtistId() == tracks.get(j).getArtistId()) {
					data.add(tracks.get(j));
				}
			}
		}
		return data;
	}
}
