package alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LoginView extends MvpView {

	void onValidationSuccess();

	void onValidationFailed(String message);
}
