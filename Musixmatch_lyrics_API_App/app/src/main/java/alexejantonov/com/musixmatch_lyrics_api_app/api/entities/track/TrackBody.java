package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackBody {

	@SerializedName("track_list")
	private List<TrackContainer> trackContainers;

	public List<TrackContainer> getTrackContainers() {
		return trackContainers;
	}

	public void setTrackContainers(List<TrackContainer> trackContainers) {
		this.trackContainers = trackContainers;
	}
}
