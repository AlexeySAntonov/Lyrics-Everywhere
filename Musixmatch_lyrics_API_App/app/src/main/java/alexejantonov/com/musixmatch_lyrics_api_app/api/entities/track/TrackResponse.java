package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track;

import com.google.gson.annotations.SerializedName;

public class TrackResponse {

	@SerializedName("message")
	private TrackMessage message;

	public TrackMessage getMessage() {
		return message;
	}

	public void setMessage(TrackMessage message) {
		this.message = message;
	}
}
