package alexejantonov.com.musixmatch_lyrics_api_app;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

	private final static String API_URL = "http://api.musixmatch.com/ws/1.1/";

	private static Retrofit retrofit;
	private OkHttpClient client;
	private static DataBase dataBase;

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);

		client = new OkHttpClient.Builder()
				.addNetworkInterceptor(new StethoInterceptor())
				.build();

		retrofit = new Retrofit.Builder()
				.client(client)
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(API_URL)
				.build();

		dataBase = new DataBase(this);
	}

	public static Retrofit getRetrofit() {
		return retrofit;
	}

	public static DataBase getDataBase() {
		return dataBase;
	}
}
