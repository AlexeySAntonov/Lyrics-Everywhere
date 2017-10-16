package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist;

import com.google.gson.annotations.SerializedName;

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData;

public class Artist implements BaseData {

	@SerializedName("artist_name")
	private String artistName;

	@SerializedName("artist_id")
	private int artistId;

	@SerializedName("artist_twitter_url")
	private String twitterUrl;

	private String topChartCountries = "";

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String getTopChartCountries() {
		return topChartCountries;
	}

	public void setTopChartCountries(String topChartCountries) {
		this.topChartCountries = topChartCountries;
	}

	@Override
	public String toString() {
		return "Artist{" +
				"artistName='" + artistName + '\'' +
				", artistId=" + artistId +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Artist)) {
			return false;
		}
		Artist artist = (Artist) o;
		return artistId == artist.artistId;
	}
}
