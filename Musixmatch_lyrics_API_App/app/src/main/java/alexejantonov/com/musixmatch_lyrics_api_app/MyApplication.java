package alexejantonov.com.musixmatch_lyrics_api_app;

import android.app.Application;
import android.content.Context;
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
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

	private static MusixMatchService service;
	private OkHttpClient client;
	private static DataBase dataBase;
	private static RequestManager imageRequestManager;

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);

		client = new OkHttpClient.Builder()
				.addNetworkInterceptor(new StethoInterceptor())
				.build();

		service = new Retrofit.Builder()
				.client(client)
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(Constants.API_URL)
				.build()
				.create(MusixMatchService.class);

		dataBase = new DataBase(this);

		imageRequestManager = Glide.with(this);
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

	public static boolean isOnline(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return info != null && info.isConnectedOrConnecting();
	}
}
