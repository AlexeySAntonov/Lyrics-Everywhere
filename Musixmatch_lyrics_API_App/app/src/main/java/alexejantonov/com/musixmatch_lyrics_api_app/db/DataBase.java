package alexejantonov.com.musixmatch_lyrics_api_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;

import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.*;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.*;

public class DataBase {

	private static final String DB_NAME = "artistsAndTracks.db";
	private static final int DB_VERSION = 1;
	private static final String EXIST_TAG = DataBase.class.getSimpleName() + "exists";

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

		db.beginTransaction();

		for (Artist artist : artists) {
			if (cursor.moveToFirst()) {
				do {
					if (artist.getArtistId() == cursor.getInt(idColumnIndex)) exists = true;
				} while (cursor.moveToNext());
			}

			if (exists) Log.d(EXIST_TAG, "Artist already exists in the db");
			else {
				ContentValues values = new ContentValues();
				values.put(COLUMN_ARTIST_ID, artist.getArtistId());
				values.put(COLUMN_ARTIST_NAME, artist.getArtistName());
				values.put(COLUMN_ARTIST_TWITTER, artist.getTwitterUrl());

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

		db.beginTransaction();

		for (Track track : tracks) {
			if (cursor.moveToFirst()) {
				do {
					if (track.getTrackId() == cursor.getInt(idColumnIndex)) exists = true;
				} while (cursor.moveToNext());
			}

			if (exists) Log.d(EXIST_TAG, "Track already exists in the db");
			else {
				ContentValues values = new ContentValues();
				values.put(COLUMN_TRACK_ID, track.getTrackId());
				values.put(COLUMN_TRACK_NAME, track.getTrackName());
				values.put(COLUMN_TRACK_ALBUM, track.getAlbumName());
				values.put(COLUMN_TRACK_ARTIST_ID, track.getArtistId());

				db.insert(TRACKS_TABLE_NAME, null, values);
			}
			exists = false;
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
	}
}
