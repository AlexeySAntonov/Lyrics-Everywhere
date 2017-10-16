package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base;

//Маркер для слияния списков
public interface BaseData {

	int getArtistId();

	void setArtistId(int artistId);

	boolean equals(Object object);
}
