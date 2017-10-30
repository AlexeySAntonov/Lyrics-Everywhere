package alexejantonov.com.musixmatch_lyrics_api_app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService;
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants;
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

	private static final String APP_PREFERENCES = MyApplication.class.getSimpleName() + ".preferences";

	private OkHttpClient client;
	private static MusixMatchService service;
	private static DataBase dataBase;
	private static RequestManager imageRequestManager;
	private static SharedPreferences preferences;

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);

		client = new OkHttpClient.Builder()
				.addNetworkInterceptor(new StethoInterceptor())
				.build();

		service = new Retrofit.Builder()
				.client(client)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(Constants.API_URL)
				.build()
				.create(MusixMatchService.class);

		dataBase = new DataBase(this);

		imageRequestManager = Glide.with(this);

		preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
	}

	public static MusixMatchService getService() {
		return service;
	}

	public static DataBase getDataBase() {
		return dataBase;
	}

	public static RequestManager getImageRequestManager() {
		return imageRequestManager;
	}

	public static SharedPreferences getPreferences() {
		return preferences;
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null && info.isConnectedOrConnecting();
	}
}
