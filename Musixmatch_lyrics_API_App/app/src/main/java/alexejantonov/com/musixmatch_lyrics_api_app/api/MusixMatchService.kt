package alexejantonov.com.musixmatch_lyrics_api_app.api

import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.API_KEY
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.COUNTRY
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.TOP_PAGE
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.TOP_PAGE_SIZE
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.TRACK_ID
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Methods.ARTISTS_GET
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Methods.LYRICS_GET
import alexejantonov.com.musixmatch_lyrics_api_app.api.config.Methods.TRACKS_GET
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistResponse
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics.LyricsResponse
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackResponse
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
}
