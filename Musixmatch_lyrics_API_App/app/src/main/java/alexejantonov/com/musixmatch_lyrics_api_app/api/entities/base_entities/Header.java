package alexejantonov.com.musixmatch_lyrics_api_app.api.entities.base_entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Header implements Serializable {

	@SerializedName("status_code")
	private int statusCode;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
