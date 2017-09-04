package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist;

import com.google.gson.annotations.SerializedName;

public class ArtistResponse {

	@SerializedName("message")
	private ArtistMessage message;

	public ArtistMessage getMessage() {
		return message;
	}

	public void setMessage(ArtistMessage message) {
		this.message = message;
	}
}
