package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData;

@StateStrategyType(AddToEndSingleStrategy.class)
interface ArtistsAndTracksListView extends MvpView {
	void showData(List<BaseData> data);
}
