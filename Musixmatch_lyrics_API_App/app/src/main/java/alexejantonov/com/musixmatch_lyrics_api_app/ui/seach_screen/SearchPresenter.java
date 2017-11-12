package alexejantonov.com.musixmatch_lyrics_api_app.ui.seach_screen;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData;
import alexejantonov.com.musixmatch_lyrics_api_app.utils.DataMergeUtil;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchFragmentView> {

	private DataBase dataBase = MyApplication.getDataBase();

	void loadData(String query) {
		List<BaseData> baseData = DataMergeUtil.searchListsMerge(dataBase.getAllArtist(), dataBase.getTracks(), dataBase.getQueryData(query));
		getViewState().showData(baseData, query);
	}
}
