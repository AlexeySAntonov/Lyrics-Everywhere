package com.aleksejantonov.lyricseverywhere.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.aleksejantonov.lyricseverywhere.ui.MainActivity
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.utils.toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_login.apiKeyEditText
import kotlinx.android.synthetic.main.activity_login.loginButton
import kotlinx.android.synthetic.main.activity_login.registerButton

class LoginActivity : MvpAppCompatActivity(), LoginView {
  private val preferences = DI.componentManager().appComponent.preferences

  @InjectPresenter
  lateinit var presenter: LoginPresenter

  @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    if (preferences.getString(Constants.API_KEY, "").isEmpty()) {
      loginButton.setOnClickListener {
        apiKeyEditText.text.toString().let { apiKey ->
          if (apiKey.length != 32) toast(String.format(getString(R.string.check_key_message), apiKey.length))
          else presenter.apiKeyValidation(apiKey)
        }
      }
      registerButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.sign_up_uri)))) }
    } else {
      finish()
      startMainActivity()
    }
  }

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    apiKeyEditText.height = loginButton.height
  }

  override fun onValidationSuccess() {
    toast(getString(R.string.validation_succeed_message))
    finish()
    startMainActivity()
  }

  override fun onValidationFailed() = toast(getString(R.string.validation_failed_message))

  private fun startMainActivity() = startActivity(MainActivity.newIntent(this))
}
