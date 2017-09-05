package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.BaseData;

public class Track implements BaseData, Serializable {

	@SerializedName("track_id")
	private int trackId;

	@SerializedName("track_name")
	private String trackName;

	@SerializedName("album_name")
	private String albumName;

	@SerializedName("artist_id")
	private int artistId;

	@SerializedName("album_coverart_100x100")
	private String albumCover;

	public int getTrackId() {
		return trackId;
	}

	public void setTrackId(int trackId) {
		this.trackId = trackId;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public int getArtistId() {
		return artistId;
	}

	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}

	public String getAlbumCover() {
		return albumCover;
	}

	public void setAlbumCover(String albumCover) {
		this.albumCover = albumCover;
	}

	@Override
	public String toString() {
		return "Track{" +
				"trackId=" + trackId +
				", trackName='" + trackName + '\'' +
				", albumName='" + albumName + '\'' +
				", artistId=" + artistId +
				", albumCover='" + albumCover + '\'' +
				'}';
	}
}
