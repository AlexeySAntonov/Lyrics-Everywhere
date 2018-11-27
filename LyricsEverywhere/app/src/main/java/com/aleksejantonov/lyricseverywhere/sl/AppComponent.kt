package com.aleksejantonov.lyricseverywhere.sl

import android.content.Context
import com.aleksejantonov.lyricseverywhere.api.MusixMatchService
import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.db.DataBase
import com.bumptech.glide.Glide
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AppComponent(private val context: Context) {
  companion object {
    private val APP_PREFERENCES = AppComponent::class.java.simpleName + ".preferences"
  }

  val dataBase by lazy { DataBase(context) }
  val imageRequestManager by lazy { Glide.with(context) }
  val preferences by lazy { context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE) }

  private val client by lazy {
    OkHttpClient.Builder()
        .addNetworkInterceptor(StethoInterceptor())
        .build()
  }
  val service by lazy {
    Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.API_URL)
        .build()
        .create(MusixMatchService::class.java)
  }
}
