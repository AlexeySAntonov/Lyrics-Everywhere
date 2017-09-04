package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track;

import com.google.gson.annotations.SerializedName;

public class TrackContainer {

	@SerializedName("track")
	private Track track;

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}
}
