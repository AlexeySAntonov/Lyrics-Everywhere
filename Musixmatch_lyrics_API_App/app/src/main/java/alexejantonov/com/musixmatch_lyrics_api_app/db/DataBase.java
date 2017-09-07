package alexejantonov.com.musixmatch_lyrics_api_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;

import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.ARTISTS_TABLE_NAME;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_ID;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_NAME;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TOP_CHART_COUNTRIES;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TWITTER;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM_COVER;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ARTIST_ID;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ID;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_NAME;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.TRACKS_TABLE_NAME;

public class DataBase {

	private static final String DB_NAME = "artistsAndTracks.db";
	private static final int DB_VERSION = 1;
	private static final String EXIST_TAG = DataBase.class.getSimpleName() + " exists";

	private DataBaseHelper dataBaseHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	private boolean exists;

	public DataBase(Context context) {
		dataBaseHelper = new DataBaseHelper(context, DB_NAME, null, DB_VERSION);
		db = dataBaseHelper.getWritableDatabase();
	}

	public void insertArtists(List<Artist> artists) {

		cursor = db.query(ARTISTS_TABLE_NAME, null, null, null, null, null, null);
		int idColumnIndex = cursor.getColumnIndex(COLUMN_ARTIST_ID);
		int countriesColumnIndex = cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES);
		String currentCountries = "";

		db.beginTransaction();

		for (Artist artist : artists) {
			if (cursor.moveToFirst()) {
				do {
					if (artist.getArtistId() == cursor.getInt(idColumnIndex)) {
						exists = true;
						currentCountries = cursor.getString(countriesColumnIndex);
						break;
					}
				} while (cursor.moveToNext());
			}

			ContentValues values = new ContentValues();
			values.put(COLUMN_ARTIST_ID, artist.getArtistId());
			values.put(COLUMN_ARTIST_NAME, artist.getArtistName());
			values.put(COLUMN_ARTIST_TWITTER, artist.getTwitterUrl());

			if (exists) {
				Log.d(EXIST_TAG, "Artist already exists in the db --> UPDATE info");
				if (!currentCountries.contains(artist.getTopChartCountries())) {
					values.put(COLUMN_ARTIST_TOP_CHART_COUNTRIES, currentCountries + artist.getTopChartCountries());
					db.update(ARTISTS_TABLE_NAME, values, "artistId=" + String.valueOf(artist.getArtistId()), null);
				}
			} else {
				values.put(COLUMN_ARTIST_TOP_CHART_COUNTRIES, artist.getTopChartCountries());
				db.insert(ARTISTS_TABLE_NAME, null, values);
			}
			exists = false;
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
	}

	public void insertTracks(List<Track> tracks) {
		cursor = db.query(TRACKS_TABLE_NAME, null, null, null, null, null, null);
		int idColumnIndex = cursor.getColumnIndex(COLUMN_TRACK_ID);
		int nameColumnIndex = cursor.getColumnIndex(COLUMN_TRACK_NAME);

		db.beginTransaction();

		for (Track track : tracks) {
			if (cursor.moveToFirst()) {
				do {
					if (track.getTrackId() == cursor.getInt(idColumnIndex) ||
							track.getTrackName().equals(cursor.getString(nameColumnIndex)))
						exists = true;
				} while (cursor.moveToNext());
			}

			if (exists) Log.d(EXIST_TAG, "Track already exists in the db");
			else {
				ContentValues values = new ContentValues();
				values.put(COLUMN_TRACK_ID, track.getTrackId());
				values.put(COLUMN_TRACK_NAME, track.getTrackName());
				values.put(COLUMN_TRACK_ALBUM, track.getAlbumName());
				values.put(COLUMN_TRACK_ARTIST_ID, track.getArtistId());
				values.put(COLUMN_TRACK_ALBUM_COVER, track.getAlbumCover());

				db.insert(TRACKS_TABLE_NAME, null, values);
			}
			exists = false;
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
	}

	public List<Artist> getArtists(String country) {
		List<Artist> artists = new ArrayList<>();
		cursor = db.query(ARTISTS_TABLE_NAME, null, "countries LIKE '%" + country + "%'", null, null, null, null);

		db.beginTransaction();

		if (cursor.moveToFirst()) {
			do {
				Artist artist = new Artist();
				artist.setArtistId(cursor.getInt(cursor.getColumnIndex(COLUMN_ARTIST_ID)));
				artist.setArtistName(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)));
				artist.setTwitterUrl(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER)));
				artist.setTopChartCountries(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES)));
				artists.add(artist);
			} while (cursor.moveToNext());
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();

		return artists;
	}

	public List<Track> getTracks() {
		List<Track> tracks = new ArrayList<>();
		cursor = db.query(TRACKS_TABLE_NAME, null, null, null, null, null, null);

		db.beginTransaction();

		if (cursor.moveToFirst()) {
			do {
				Track track = new Track();
				track.setTrackId(cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_ID)));
				track.setTrackName(cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_NAME)));
				track.setAlbumName(cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM)));
				track.setArtistId(cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_ARTIST_ID)));
				track.setAlbumCover(cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM_COVER)));
				tracks.add(track);
			} while (cursor.moveToNext());
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();

		return tracks;
	}

	public List<Artist> getQueryArtists(String queryName) {
		List<Artist> artists = new ArrayList<>();
		cursor = db.query(ARTISTS_TABLE_NAME, null, "name LIKE '%" + queryName + "%'", null, null, null, null);

		db.beginTransaction();

		if (cursor.moveToFirst()) {
			do {
				Artist artist = new Artist();
				artist.setArtistId(cursor.getInt(cursor.getColumnIndex(COLUMN_ARTIST_ID)));
				artist.setArtistName(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)));
				artist.setTwitterUrl(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER)));
				artist.setTopChartCountries(cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES)));
				artists.add(artist);
			} while (cursor.moveToNext());
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();

		return artists;
	}
}
