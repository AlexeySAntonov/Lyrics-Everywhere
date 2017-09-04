package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics;

import com.google.gson.annotations.SerializedName;

public class LyricsBody {

	@SerializedName("lyrics")
	private Lyrics lyrics;

	public Lyrics getLyrics() {
		return lyrics;
	}

	public void setLyrics(Lyrics lyrics) {
		this.lyrics = lyrics;
	}
}
