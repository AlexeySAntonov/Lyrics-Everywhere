package alexejantonov.com.musixmatch_lyrics_api_app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static List<BaseData> searchListsMerge(List<Artist> artists, List<Track> tracks, List<BaseData> data) {
		List<BaseData> mergeData = new ArrayList<>();
		Map<Artist, List<Track>> dataMap = new HashMap<>();

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i) instanceof Artist) {
				for (int j = 0; j < tracks.size(); j++) {
					if (data.get(i).getArtistId() == tracks.get(j).getArtistId()) {
						if (dataMap.containsKey(data.get(i))) {
							dataMap.get(data.get(i)).add(tracks.get(j));
						} else {
							List<Track> mapBucketTracks = new ArrayList<>();
							mapBucketTracks.add(tracks.get(j));
							dataMap.put((Artist) data.get(i), mapBucketTracks);
						}
					}
				}
			} else if (data.get(i) instanceof Track) {
				for (int j = 0; j < artists.size(); j++) {
					if (data.get(i).getArtistId() == artists.get(j).getArtistId()) {
						if (dataMap.containsKey(artists.get(j))) {
							dataMap.get(artists.get(j)).add((Track) data.get(i));
							break;
						} else {
							List<Track> mapBucketTrack = new ArrayList<>();
							mapBucketTrack.add((Track) data.get(i));
							dataMap.put(artists.get(j), mapBucketTrack);
							break;
						}
					}
				}
			}
		}

		for (Artist artist : dataMap.keySet()) {
			mergeData.add(artist);
			mergeData.addAll(dataMap.get(artist));
		}

		return mergeData;
	}
}
