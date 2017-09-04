package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics;

import com.google.gson.annotations.SerializedName;

public class Lyrics {

	@SerializedName("lyrics_body")
	private String lyricsText;

	public String getLyricsText() {
		return lyricsText;
	}

	public void setLyricsText(String lyricsText) {
		this.lyricsText = lyricsText;
	}
}
