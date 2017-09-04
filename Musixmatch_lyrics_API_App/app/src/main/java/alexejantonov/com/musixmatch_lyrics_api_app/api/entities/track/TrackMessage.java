package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track;

import com.google.gson.annotations.SerializedName;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.base_entities.Header;

public class TrackMessage {

	@SerializedName("header")
	private Header header;

	@SerializedName("body")
	private TrackBody body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public TrackBody getBody() {
		return body;
	}

	public void setBody(TrackBody body) {
		this.body = body;
	}
}
