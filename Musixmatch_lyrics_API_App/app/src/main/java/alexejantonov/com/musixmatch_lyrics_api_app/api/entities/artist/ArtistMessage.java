package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist;

import com.google.gson.annotations.SerializedName;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.base_entities.Header;

public class ArtistMessage {

	@SerializedName("header")
	private Header header;

	@SerializedName("body")
	private ArtistBody body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public ArtistBody getBody() {
		return body;
	}

	public void setBody(ArtistBody body) {
		this.body = body;
	}
}
