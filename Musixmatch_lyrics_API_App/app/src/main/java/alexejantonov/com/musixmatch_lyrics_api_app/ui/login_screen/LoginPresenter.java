package alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class LoginPresenter extends MvpPresenter<LoginView> {

	private CompositeDisposable subscriptions = new CompositeDisposable();

	void tokenValidation(String token) {
		subscriptions.add(MyApplication.getService().getArtists(token, QueryType.ru.name(), "1", "1")
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(
						response -> {
							MyApplication.getPreferences().edit().putString(Constants.API_KEY, token).commit();
							getViewState().onValidationSuccess();
						},
						e -> {
							getViewState().onValidationFailed("check api key value");
							Log.d("Validation failed", Log.getStackTraceString(e));
						}
				)
		);
	}

	@Override
	public void detachView(LoginView view) {
		subscriptions.clear();
		super.detachView(view);
	}
}
