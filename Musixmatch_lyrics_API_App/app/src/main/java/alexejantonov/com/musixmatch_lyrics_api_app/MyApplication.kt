package alexejantonov.com.musixmatch_lyrics_api_app

import alexejantonov.com.musixmatch_lyrics_api_app.api.MusixMatchService
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBase
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.leakcanary.LeakCanary
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

  private var client: OkHttpClient? = null

  override fun onCreate() {
    super.onCreate()
    Stetho.initializeWithDefaults(this)

    if (BuildConfig.DEBUG) {
      if (LeakCanary.isInAnalyzerProcess(this)) return
      LeakCanary.install(this)
    }

    client = OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    service = Retrofit.Builder()
        .client(client!!)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.API_URL)
        .build()
        .create(MusixMatchService::class.java)

    dataBase = DataBase(this)

    imageRequestManager = Glide.with(this)

    preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
  }

  companion object {

    private val APP_PREFERENCES = MyApplication::class.java.simpleName + ".preferences"
    lateinit var service: MusixMatchService
    lateinit var dataBase: DataBase
    lateinit var imageRequestManager: RequestManager
    lateinit var preferences: SharedPreferences

    fun isOnline(context: Context): Boolean {
      val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val info = connectivityManager.activeNetworkInfo
      return info != null && info.isConnectedOrConnecting
    }
  }
}
