package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistBody {

	@SerializedName("artist_list")
	private List<ArtistContainer> artistContainers;

	public List<ArtistContainer> getArtistContainers() {
		return artistContainers;
	}

	public void setArtistContainers(List<ArtistContainer> artistContainers) {
		this.artistContainers = artistContainers;
	}
}
