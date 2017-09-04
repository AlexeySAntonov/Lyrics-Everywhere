package alexejantonov.com.musixmatch_lyrics_api_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.*;
import static alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.*;

public class DataBaseHelper extends SQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + ARTISTS_TABLE_NAME + " (" +
				COLUMN_ARTIST_ID + " INTEGER, " +
				COLUMN_ARTIST_NAME + " TEXT, " +
				COLUMN_ARTIST_TWITTER + " TEXT)");

		db.execSQL("CREATE TABLE " + TRACKS_TABLE_NAME + " (" +
				COLUMN_TRACK_ID + " INTEGER, " +
				COLUMN_TRACK_NAME + " TEXT, " +
				COLUMN_TRACK_ALBUM + " TEXT, " +
				COLUMN_TRACK_ARTIST_ID + " INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
