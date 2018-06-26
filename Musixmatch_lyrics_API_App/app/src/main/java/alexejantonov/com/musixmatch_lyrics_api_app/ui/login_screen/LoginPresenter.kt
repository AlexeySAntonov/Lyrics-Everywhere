package alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen

import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>() {

  private val subscriptions = CompositeDisposable()

  override fun detachView(view: LoginView) {
    subscriptions.clear()
    super.detachView(view)
  }

  fun apiKeyValidation(apiKey: String) {
    subscriptions.add(MyApplication.service.getArtists(apiKey, QueryType.RU.name, "1", "1")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(
            {
              MyApplication.preferences.edit().putString(Constants.API_KEY, apiKey).apply()
              viewState.onValidationSuccess()
            },
            { e ->
              viewState.onValidationFailed("check api key value")
              Log.d("Validation failed", Log.getStackTraceString(e))
            }
        )
    )
  }
}
