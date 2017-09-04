package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics;

import com.google.gson.annotations.SerializedName;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.base_entities.Header;

public class LyricsMessage {

	@SerializedName("header")
	private Header header;

	@SerializedName("body")
	private LyricsBody body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public LyricsBody getBody() {
		return body;
	}

	public void setBody(LyricsBody body) {
		this.body = body;
	}
}
