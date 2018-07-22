package alexejantonov.com.musixmatch_lyrics_api_app.db

import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.ARTISTS_TABLE_NAME
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_ID
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_NAME
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TOP_CHART_COUNTRIES
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TWITTER
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM_COVER
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ARTIST_ID
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_ID
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.COLUMN_TRACK_NAME
import alexejantonov.com.musixmatch_lyrics_api_app.db.DataBaseContract.TracksTable.TRACKS_TABLE_NAME
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

  override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("CREATE TABLE " + ARTISTS_TABLE_NAME + " (" +
        COLUMN_ARTIST_ID + " INTEGER, " +
        COLUMN_ARTIST_NAME + " TEXT, " +
        COLUMN_ARTIST_TWITTER + " TEXT, " +
        COLUMN_ARTIST_TOP_CHART_COUNTRIES + " TEXT)")

    db.execSQL("CREATE TABLE " + TRACKS_TABLE_NAME + " (" +
        COLUMN_TRACK_ID + " INTEGER, " +
        COLUMN_TRACK_NAME + " TEXT, " +
        COLUMN_TRACK_ALBUM + " TEXT, " +
        COLUMN_TRACK_ARTIST_ID + " INTEGER, " +
        COLUMN_TRACK_ALBUM_COVER + " TEXT)")
  }

  override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

  }
}
