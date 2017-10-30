package alexejantonov.com.musixmatch_lyrics_api_app.api;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics.LyricsResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.API_KEY;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.COUNTRY;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.TOP_PAGE;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.TOP_PAGE_SIZE;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Constants.TRACK_ID;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Methods.ARTISTS_GET;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Methods.LYRICS_GET;
import static alexejantonov.com.musixmatch_lyrics_api_app.api.config.Methods.TRACKS_GET;

public interface MusixMatchService {

	@GET(ARTISTS_GET)
	Single<ArtistResponse> getArtists(@Query(API_KEY) String apiKey,
	                                  @Query(COUNTRY) String country,
	                                  @Query(TOP_PAGE) String page,
	                                  @Query(TOP_PAGE_SIZE) String pageSize);

	@GET(TRACKS_GET)
	Single<TrackResponse> getTracks(@Query(API_KEY) String apiKey,
	                                @Query(COUNTRY) String country,
	                                @Query(TOP_PAGE) String page,
	                                @Query(TOP_PAGE_SIZE) String pageSize);

	@GET(LYRICS_GET)
	Single<LyricsResponse> getLyrics(@Query(API_KEY) String apiKey,
	                                 @Query(TRACK_ID) String trackId);
}
