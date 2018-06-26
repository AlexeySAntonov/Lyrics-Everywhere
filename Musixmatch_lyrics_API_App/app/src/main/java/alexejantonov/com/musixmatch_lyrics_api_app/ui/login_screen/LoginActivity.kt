package alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen

import alexejantonov.com.musixmatch_lyrics_api_app.MainActivity
import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication
import alexejantonov.com.musixmatch_lyrics_api_app.R
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_login.apiKeyEditText
import kotlinx.android.synthetic.main.activity_login.loginButton
import kotlinx.android.synthetic.main.activity_login.registerButton

class LoginActivity : MvpAppCompatActivity(), LoginView {

  private var apiKey: String = ""
  private val preferences = MyApplication.preferences

  @InjectPresenter
  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    if (preferences.getString(Constants.API_KEY, "").isEmpty()) {
      loginButton.setOnClickListener {
        apiKey = apiKeyEditText.text.toString()
        if (apiKey.length != 32) {
          Toast.makeText(
              this,
              "Check input key: expected length = 32, current = " + apiKey.length,
              Toast.LENGTH_LONG).show()
        } else {
          presenter.apiKeyValidation(apiKey)
        }
      }
      registerButton.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.musixmatch.com/signup"))) }
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
    Toast.makeText(this, "Validation successful", Toast.LENGTH_LONG).show()
    finish()
    startMainActivity()
  }

  override fun onValidationFailed(message: String) {
    Toast.makeText(this, "Validation failed: $message", Toast.LENGTH_LONG).show()
  }

  private fun startMainActivity() {
    val intent = MainActivity.newIntent(this)
    startActivity(intent)
  }
}
