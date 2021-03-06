package com.aleksejantonov.lyricseverywhere.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.AlbumsTable.ALBUMS_TABLE_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.AlbumsTable.COLUMN_ALBUM_COVER
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.AlbumsTable.COLUMN_ALBUM_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.AlbumsTable.COLUMN_ALBUM_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.ARTISTS_TABLE_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TOP_CHART_COUNTRIES
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TWITTER
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ARTIST_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.TRACKS_TABLE_NAME

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
        COLUMN_TRACK_ARTIST_ID + " INTEGER)")

    db.execSQL("CREATE TABLE " + ALBUMS_TABLE_NAME + " (" +
        COLUMN_ALBUM_ID + " INTEGER, " +
        COLUMN_ALBUM_NAME + " TEXT, " +
        COLUMN_ALBUM_COVER + " TEXT)")
  }

  override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

  }
}
