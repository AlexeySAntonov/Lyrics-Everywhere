package alexejantonov.com.musixmatch_lyrics_api_app.api;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.ArtistResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.lyrics.LyricsResponse;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.TrackResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusixMatchService {

	@GET("chart.artists.get?apikey=a15ab4dde789e7a4b63d2db9000abb0e")
	Call<ArtistResponse> getArtists(@Query("country") String country,
	                                @Query("page") String page,
	                                @Query("page_size") String pageSize);

	@GET("chart.tracks.get?apikey=a15ab4dde789e7a4b63d2db9000abb0e")
	Call<TrackResponse> getTracks(@Query("country") String country,
	                              @Query("page") String page,
	                              @Query("page_size") String pageSize);

	@GET("track.lyrics.get?apikey=a15ab4dde789e7a4b63d2db9000abb0e")
	Call<LyricsResponse> getLyrics(@Query("track_id") String trackId);
}
