package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import java.util.List;

public interface ArtistsAndTracksScreenContract {

	interface View {
		void showData(List<BaseData> data);
	}

	interface Presenter {
		void onAttach(View view, String country);
		void onDetach();

		void loadData();
	}
}
