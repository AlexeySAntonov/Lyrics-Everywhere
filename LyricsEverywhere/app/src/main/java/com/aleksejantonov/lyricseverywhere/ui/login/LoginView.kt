package com.aleksejantonov.lyricseverywhere.ui.login

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LoginView : MvpView {
  fun onValidationSuccess()
  fun onValidationFailed(message: String)
}
