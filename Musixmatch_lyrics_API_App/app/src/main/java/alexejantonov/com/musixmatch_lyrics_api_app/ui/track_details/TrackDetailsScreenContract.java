package alexejantonov.com.musixmatch_lyrics_api_app.ui.track_details;

public interface TrackDetailsScreenContract {

	interface View {
		void showData(String lyricsText);
	}

	interface Presenter {
		void onAttach(View view);
		void onDetach();
		void setTrackId(String trackId);
		void loadLyrics();
	}
}
