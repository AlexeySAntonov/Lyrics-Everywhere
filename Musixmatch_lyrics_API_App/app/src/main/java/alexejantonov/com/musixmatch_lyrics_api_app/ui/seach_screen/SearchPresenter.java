package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchFragmentView> {

	private DataBase dataBase = MyApplication.getDataBase();

	void loadData(String query) {
		getViewState().showData(DataMergeUtil.listsMerge(dataBase.getQueryArtists(query), dataBase.getTracks()));
	}
}
