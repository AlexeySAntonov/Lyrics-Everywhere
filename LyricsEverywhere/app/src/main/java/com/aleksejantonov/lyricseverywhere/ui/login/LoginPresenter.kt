package com.aleksejantonov.lyricseverywhere.ui.login

import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.base.QueryType
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class LoginPresenter : MvpPresenter<LoginView>() {

  private val subscriptions = CompositeDisposable()

  override fun detachView(view: LoginView) {
    subscriptions.clear()
    super.detachView(view)
  }

  fun apiKeyValidation(apiKey: String) {
    subscriptions.add(DI.componentManager().appComponent.service.getArtists(apiKey, QueryType.RU.name, "1", "1")
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(
            {
              DI.componentManager().appComponent.preferences.edit().putString(Constants.API_KEY, apiKey).apply()
              viewState.onValidationSuccess()
            },
            {
              viewState.onValidationFailed("check api key value")
              Timber.d("Validation failed: ${it.message}")
            }
        )
    )
  }
}
