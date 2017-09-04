package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics;

import com.google.gson.annotations.SerializedName;

public class LyricsResponse {

	@SerializedName("message")
	private LyricsMessage message;

	public LyricsMessage getMessage() {
		return message;
	}

	public void setMessage(LyricsMessage message) {
		this.message = message;
	}
}
