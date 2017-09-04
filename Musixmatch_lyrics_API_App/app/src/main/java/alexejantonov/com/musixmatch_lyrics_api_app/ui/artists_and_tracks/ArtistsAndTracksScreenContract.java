package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.content.Context;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchApi;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;

public interface ArtistsAndTracksScreenContract {

	interface View {
		void showData(List<BaseData> data);
	}

	interface Presenter {
		void onAttach(DataBase dataBase, MusixMatchApi musixMatchApi, View view, String country);
		void onDetach();
		void loadArtists();
		void loadTracks();
	}
}
