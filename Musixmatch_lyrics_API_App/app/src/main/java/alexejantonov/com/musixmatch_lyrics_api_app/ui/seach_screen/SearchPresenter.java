package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil;

import static alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen.SeachScreenContract.Presenter;
import static alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen.SeachScreenContract.View;

public class SearchPresenter implements Presenter {

	private View view;
	private DataBase dataBase = MyApplication.getDataBase();

	@Override
	public void onAttach(View view) {
		this.view = view;
	}

	@Override
	public void onDetach() {
		view = null;
	}

	@Override
	public void loadData(String query) {
		view.showData(DataMergeUtil.listsMerge(dataBase.getQueryArtists(query), dataBase.getTracks()));
	}
}
