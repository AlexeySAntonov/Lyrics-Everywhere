package alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import alexejantonov.com.musixmatch_lyrics_api_app.MainActivity;
import alexejantonov.com.musixmatch_lyrics_api_app.MyApplication;
import alexejantonov.com.musixmatch_lyrics_api_app.R;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;

public class LoginActivity extends MvpAppCompatActivity implements LoginView {

	private String token;
	private SharedPreferences preferences = MyApplication.getPreferences();

	private Button loginButton;
	private Button registerButton;
	private EditText apikeyEditText;

	@InjectPresenter
	LoginPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (preferences.getString(Constants.API_KEY, "").isEmpty()) {
			loginButton = findViewById(R.id.loginButton);
			registerButton = findViewById(R.id.registerButton);
			apikeyEditText = findViewById(R.id.apiKeyEditText);

			loginButton.setOnClickListener(view -> {
				token = apikeyEditText.getText().toString();
				if (token.length() != 32) {
					Toast.makeText(
							LoginActivity.this,
							"Check input key: expected length = 32, current = " + token.length(),
							Toast.LENGTH_LONG).show();
				} else {
					presenter.tokenValidation(token);
				}
			});
		} else {
			finish();
			startMainActivity();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		apikeyEditText.setHeight(loginButton.getHeight());
	}

	@Override
	public void onValidationSuccess() {
		Toast.makeText(LoginActivity.this, "Validation successful", Toast.LENGTH_LONG).show();
		finish();
		startMainActivity();
	}

	@Override
	public void onValidationFailed(String message) {
		Toast.makeText(LoginActivity.this, "Validation failed: " + message, Toast.LENGTH_LONG).show();
	}

	private void startMainActivity() {
		Intent intent = MainActivity.newIntent(this);
		startActivity(intent);
	}
}
