package com.aleksejantonov.lyricseverywhere.db

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.aleksejantonov.lyricseverywhere.api.entities.album.Album
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
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
import timber.log.Timber
import java.util.ArrayList

class DataBase(context: Context) {

  companion object {
    private const val DB_NAME = "artistsAndTracks.db"
    private const val DB_VERSION = 1
    private val EXIST_TAG = DataBase::class.java.simpleName + " exists"
  }

  private val dataBaseHelper: DataBaseHelper
  private val db: SQLiteDatabase
  private var exists: Boolean = false

  init {
    dataBaseHelper = DataBaseHelper(context, DB_NAME, null, DB_VERSION)
    db = dataBaseHelper.writableDatabase
  }

  val allArtist: List<Artist>
    get() {
      val artists = ArrayList<Artist>()
      val cursor = db.query(ARTISTS_TABLE_NAME, null, null, null, null, null, null)

      db.beginTransaction()
      try {
        if (cursor.moveToFirst()) {
          do {
            val artist = Artist(
                artistId = cursor.getLong(cursor.getColumnIndex(COLUMN_ARTIST_ID)),
                artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)),
                twitterUrl = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER))
            )
            artist.topChartCountries = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES))
            artists.add(artist)
          } while (cursor.moveToNext())
        }

        db.setTransactionSuccessful()
      } catch (e: SQLException) {
        Timber.e(e)
      } finally {
        db.endTransaction()
        cursor.close()
      }

      return artists
    }

  val tracks: List<Track>
    get() {
      val tracks = ArrayList<Track>()
      val cursor = db.query(TRACKS_TABLE_NAME, null, null, null, null, null, null)

      db.beginTransaction()
      try {
        if (cursor.moveToFirst()) {
          do {
            val track = Track(
                trackId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRACK_ID)),
                trackName = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_NAME)),
                albumName = cursor.getString(cursor.getColumnIndex(COLUMN_TRACK_ALBUM)),
                artistId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRACK_ARTIST_ID))
            )
            tracks.add(track)
          } while (cursor.moveToNext())
        }

        db.setTransactionSuccessful()
      } catch (e: SQLException) {
        Timber.e(e)
      } finally {
        db.endTransaction()
        cursor.close()
      }

      return tracks
    }

  fun insertArtists(artists: List<Artist>) {
    val cursor = db.query(ARTISTS_TABLE_NAME, null, null, null, null, null, null)
    var currentCountries = ""

    db.beginTransaction()
    try {
      val idColumnIndex = cursor.getColumnIndex(COLUMN_ARTIST_ID)
      val countriesColumnIndex = cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES)

      for (artist in artists) {
        if (cursor.moveToFirst()) {
          do {
            if (artist.artistId == cursor.getLong(idColumnIndex)) {
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
    } catch (e: SQLException) {
      Timber.e(e)
    } finally {
      db.endTransaction()
      cursor.close()
    }
  }

  fun insertTracks(tracks: List<Track>) {
    val cursor = db.query(TRACKS_TABLE_NAME, null, null, null, null, null, null)

    db.beginTransaction()
    try {
      val idColumnIndex = cursor.getColumnIndex(COLUMN_TRACK_ID)
      val nameColumnIndex = cursor.getColumnIndex(COLUMN_TRACK_NAME)

      for (track in tracks) {
        if (cursor.moveToFirst()) {
          do {
            if (track.trackId == cursor.getLong(idColumnIndex) || track.trackName == cursor.getString(nameColumnIndex))
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

          db.insert(TRACKS_TABLE_NAME, null, values)
        }
        exists = false
      }

      db.setTransactionSuccessful()
    } catch (e: SQLException) {
      Timber.e(e)
    } finally {
      db.endTransaction()
      cursor.close()
    }
  }

  fun insertAlbums(albums: List<Album>) {
    val cursor = db.query(ALBUMS_TABLE_NAME, null, null, null, null, null, null)
    val idColumnIndex = cursor.getColumnIndex(COLUMN_ALBUM_ID)

    db.beginTransaction()

    for (album in albums) {
      if (cursor.moveToFirst()) {
        do {
          if (album.albumId == cursor.getLong(idColumnIndex)) exists = true
        } while (cursor.moveToNext())
      }

      if (exists)
        Timber.d(EXIST_TAG, "Album already exists in the db")
      else {
        val values = ContentValues()
        values.put(COLUMN_ALBUM_ID, album.albumId)
        values.put(COLUMN_ALBUM_NAME, album.albumName)
        values.put(COLUMN_ALBUM_COVER, album.cover)

        db.insert(ALBUMS_TABLE_NAME, null, values)
      }
      exists = false
    }

    db.setTransactionSuccessful()
    db.endTransaction()
    cursor.close()
  }

  fun getCountryArtists(country: String?): List<Artist> {
    val artists = ArrayList<Artist>()
    val cursor = db.query(ARTISTS_TABLE_NAME, null, "countries LIKE '%$country%'", null, null, null, null)

    db.beginTransaction()
    try {
      if (cursor.moveToFirst()) {
        do {
          val artist = Artist(
              artistId = cursor.getLong(cursor.getColumnIndex(COLUMN_ARTIST_ID)),
              artistName = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_NAME)),
              twitterUrl = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TWITTER))
          )
          artist.topChartCountries = cursor.getString(cursor.getColumnIndex(COLUMN_ARTIST_TOP_CHART_COUNTRIES))
          artists.add(artist)
        } while (cursor.moveToNext())
      }

      db.setTransactionSuccessful()
    } catch (e: SQLException) {
      Timber.e(e)
    } finally {
      db.endTransaction()
      cursor.close()
    }

    return artists
  }

}
