package com.aleksejantonov.lyricseverywhere.db

class DataBaseContract {

  object ArtistsTable {
    const val ARTISTS_TABLE_NAME = "artists"

    const val COLUMN_ARTIST_ID = "artistId"
    const val COLUMN_ARTIST_NAME = "name"
    const val COLUMN_ARTIST_TWITTER = "twitter"
    const val COLUMN_ARTIST_TOP_CHART_COUNTRIES = "countries"
  }

  object TracksTable {
    const val TRACKS_TABLE_NAME = "tracks"

    const val COLUMN_TRACK_ID = "trackId"
    const val COLUMN_TRACK_NAME = "name"
    const val COLUMN_TRACK_ALBUM = "album"
    const val COLUMN_TRACK_ARTIST_ID = "artistId"
  }

  object AlbumsTable {
    const val ALBUMS_TABLE_NAME = "albums"

    const val COLUMN_ALBUM_ID = "albumId"
    const val COLUMN_ALBUM_NAME = "name"
    const val COLUMN_ALBUM_COVER = "cover"
  }
}
