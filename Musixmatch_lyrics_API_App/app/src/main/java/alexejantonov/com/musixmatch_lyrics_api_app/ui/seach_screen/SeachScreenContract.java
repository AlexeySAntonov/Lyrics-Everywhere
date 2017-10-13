package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData;

public interface SeachScreenContract {

	interface View {
		void showData(List<BaseData> data);
	}

	interface Presenter {
		void onAttach(View view);

		void onDetach();

		void loadData(String query);
	}
}
