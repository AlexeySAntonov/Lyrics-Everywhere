package com.aleksejantonov.lyricseverywhere.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.ARTISTS_TABLE_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TOP_CHART_COUNTRIES
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.ArtistsTable.COLUMN_ARTIST_TWITTER
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ALBUM_COVER
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ARTIST_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_ID
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.COLUMN_TRACK_NAME
import com.aleksejantonov.lyricseverywhere.db.DataBaseContract.TracksTable.TRACKS_TABLE_NAME
import com.aleksejantonov.lyricseverywhere.ui.base.BaseData
import timber.log.Timber
import java.util.ArrayList
import java.util.Collections

class DataBase(context: Context) {

  companion object {
    private const val DB_NAME = "artistsAndTracks.db"
    private const val DB_VERSION = 1
    private val EXIST_TAG = DataBase::class.java.simpleName + " exists"
  }

  private val dataBaseHelper: DataBaseHelper
  private val db: SQLiteDatabase
  private lateinit var cursor: Cursor
  private var exists: Boolean = false

  init {
    dataBaseHelper = DataBaseHelper(context, DB_NAME, null, DB_VERSION)
    db = dataBaseHelper.writableDatabase
  }

  val allArtist: List<Artist>
    get() {
      val artists = ArrayList<Artist>()
      cursor = db.query(ARTISTS_TABLE_NAME, null, null, null, null, null, null)

      db.beginTransaction()

      if (cursor.moveToFirst()) {
        do {
          val artist = Artist(
              artistId = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTIST_ID)),
              artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)),
              twitterUrl = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER))
          )
          artist.topChartCountries = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES))
          artists.add(artist)
        } while (cursor.moveToNext())
      }

      db.setTransactionSuccessful()
      db.endTransaction()
      cursor.close()

      return artists
    }

  val tracks: List<Track>
    get() {
      val tracks = ArrayList<Track>()
      cursor = db.query(TRACKS_TABLE_NAME, null, null, null, null, null, null)

      db.beginTransaction()

      if (cursor.moveToFirst()) {
        do {
          val track = Track(
              trackId = cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_ID)),
              trackName = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_NAME)),
              albumName = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM)),
              artistId = cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_ARTIST_ID)),
              albumCover = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM_COVER))
          )
          tracks.add(track)
        } while (cursor.moveToNext())
      }

      db.setTransactionSuccessful()
      db.endTransaction()
      cursor.close()

      return tracks
    }

  fun insertArtists(artists: List<Artist>) {

    cursor = db.query(ARTISTS_TABLE_NAME, null, null, null, null, null, null)
    val idColumnIndex = cursor.getColumnIndex(COLUMN_ARTIST_ID)
    val countriesColumnIndex = cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES)
    var currentCountries = ""

    db.beginTransaction()

    for (artist in artists) {
      if (cursor.moveToFirst()) {
        do {
          if (artist.artistId == cursor.getInt(idColumnIndex)) {
            exists = true
            currentCountries = cursor.getString(countriesColumnIndex)
            break
          }
        } while (cursor.moveToNext())
      }

      val values = ContentValues()
      values.put(COLUMN_ARTIST_ID, artist.artistId)
      values.put(COLUMN_ARTIST_NAME, artist.artistName)
      values.put(COLUMN_ARTIST_TWITTER, artist.twitterUrl)

      if (exists) {
        Timber.d(EXIST_TAG, "Artist already exists in the db --> UPDATE info")
        if (!currentCountries.contains(artist.topChartCountries)) {
          values.put(COLUMN_ARTIST_TOP_CHART_COUNTRIES, currentCountries + artist.topChartCountries)
          db.update(ARTISTS_TABLE_NAME, values, "artistId=${artist.artistId}", null)
        }
      } else {
        values.put(COLUMN_ARTIST_TOP_CHART_COUNTRIES, artist.topChartCountries)
        db.insert(ARTISTS_TABLE_NAME, null, values)
      }
      exists = false
    }

    db.setTransactionSuccessful()
    db.endTransaction()
    cursor.close()
  }

  fun insertTracks(tracks: List<Track>) {
    cursor = db.query(TRACKS_TABLE_NAME, null, null, null, null, null, null)
    val idColumnIndex = cursor.getColumnIndex(COLUMN_TRACK_ID)
    val nameColumnIndex = cursor.getColumnIndex(COLUMN_TRACK_NAME)

    db.beginTransaction()

    for (track in tracks) {
      if (cursor.moveToFirst()) {
        do {
          if (track.trackId == cursor.getInt(idColumnIndex) || track.trackName == cursor.getString(nameColumnIndex))
            exists = true
        } while (cursor.moveToNext())
      }

      if (exists)
        Timber.d(EXIST_TAG, "Track already exists in the db")
      else {
        val values = ContentValues()
        values.put(COLUMN_TRACK_ID, track.trackId)
        values.put(COLUMN_TRACK_NAME, track.trackName)
        values.put(COLUMN_TRACK_ALBUM, track.albumName)
        values.put(COLUMN_TRACK_ARTIST_ID, track.artistId)
        values.put(COLUMN_TRACK_ALBUM_COVER, track.albumCover)

        db.insert(TRACKS_TABLE_NAME, null, values)
      }
      exists = false
    }

    db.setTransactionSuccessful()
    db.endTransaction()
    cursor.close()
  }

  fun getCountryArtists(country: String?): List<Artist> {
    val artists = ArrayList<Artist>()
    cursor = db.query(ARTISTS_TABLE_NAME, null, "countries LIKE '%$country%'", null, null, null, null)

    db.beginTransaction()

    if (cursor.moveToFirst()) {
      do {
        val artist = Artist(
            artistId = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTIST_ID)),
            artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)),
            twitterUrl = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER))
        )
        artist.topChartCountries = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES))
        artists.add(artist)
      } while (cursor.moveToNext())
    }

    db.setTransactionSuccessful()
    db.endTransaction()
    cursor.close()

    return artists
  }

  @Deprecated("Expensive search")
  fun getQueryData(queryName: String): List<BaseData> {
    val artists = ArrayList<Artist>()
    cursor = db.query(ARTISTS_TABLE_NAME, null, "name LIKE '%$queryName%'", null, null, null, null)

    db.beginTransaction()

    if (cursor.moveToFirst()) {
      do {
        val artist = Artist(
            artistId = cursor.getInt(cursor.getColumnIndex(COLUMN_ARTIST_ID)),
            artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)),
            twitterUrl = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER))
        )
        artist.topChartCountries = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES))
        artists.add(artist)
      } while (cursor.moveToNext())
    }

    db.setTransactionSuccessful()
    db.endTransaction()
    cursor.close()

    val tracks = ArrayList<Track>()
    cursor = db.query(TRACKS_TABLE_NAME, null, "name LIKE '%$queryName%'", null, null, null, null)
    db.beginTransaction()

    if (cursor.moveToFirst()) {
      do {
        val track = Track(
            trackId = cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_ID)),
            trackName = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_NAME)),
            albumName = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM)),
            albumCover = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM_COVER)),
            artistId = cursor.getInt(cursor.getColumnIndex(COLUMN_TRACK_ARTIST_ID))
        )
        tracks.add(track)
      } while (cursor.moveToNext())
    }

    db.setTransactionSuccessful()
    db.endTransaction()
    cursor.close()

    val data = object : ArrayList<BaseData>() {
      init {
        addAll(artists)
        addAll(tracks)
      }
    }

    Collections.shuffle(data)

    return data
  }
}
