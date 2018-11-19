package com.aleksejantonov.lyricseverywhere.api

import com.aleksejantonov.lyricseverywhere.api.config.Constants.API_KEY
import com.aleksejantonov.lyricseverywhere.api.config.Constants.COUNTRY
import com.aleksejantonov.lyricseverywhere.api.config.Constants.QUERY
import com.aleksejantonov.lyricseverywhere.api.config.Constants.TOP_PAGE
import com.aleksejantonov.lyricseverywhere.api.config.Constants.TOP_PAGE_SIZE
import com.aleksejantonov.lyricseverywhere.api.config.Constants.TRACK_ID
import com.aleksejantonov.lyricseverywhere.api.config.Methods.ARTISTS_GET
import com.aleksejantonov.lyricseverywhere.api.config.Methods.LYRICS_GET
import com.aleksejantonov.lyricseverywhere.api.config.Methods.TRACKS_GET
import com.aleksejantonov.lyricseverywhere.api.config.Methods.TRACK_SEARCH
import com.aleksejantonov.lyricseverywhere.api.entities.artist.ArtistResponse
import com.aleksejantonov.lyricseverywhere.api.entities.lyrics.LyricsResponse
import com.aleksejantonov.lyricseverywhere.api.entities.track.TrackResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MusixMatchService {

  @GET(ARTISTS_GET)
  fun getArtists(
      @Query(API_KEY) apiKey: String,
      @Query(COUNTRY) country: String?,
      @Query(TOP_PAGE) page: String,
      @Query(TOP_PAGE_SIZE) pageSize: String
  ): Single<ArtistResponse>

  @GET(TRACKS_GET)
  fun getTracks(
      @Query(API_KEY) apiKey: String,
      @Query(COUNTRY) country: String?,
      @Query(TOP_PAGE) page: String,
      @Query(TOP_PAGE_SIZE) pageSize: String
  ): Single<TrackResponse>

  @GET(LYRICS_GET)
  fun getLyrics(
      @Query(API_KEY) apiKey: String,
      @Query(TRACK_ID) trackId: String
  ): Single<LyricsResponse>

  @GET(TRACK_SEARCH)
  fun trackSearch(
      @Query(API_KEY) apiKey: String,
      @Query(QUERY) query: String
  ): Single<TrackResponse>
}
