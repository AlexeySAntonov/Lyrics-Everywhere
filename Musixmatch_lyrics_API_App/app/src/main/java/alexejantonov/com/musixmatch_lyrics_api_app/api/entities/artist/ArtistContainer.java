package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist;

import com.google.gson.annotations.SerializedName;

public class ArtistContainer {

	@SerializedName("artist")
	private Artist artist;

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}
}
