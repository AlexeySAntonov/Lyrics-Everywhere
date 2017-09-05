package alexejantonov.com.musixmatch_lyrics_api_app.db;

class DataBaseContract {

	class ArtistsTable {

		static final String ARTISTS_TABLE_NAME = "artists";

		static final String COLUMN_ARTIST_ID = "artistId";
		static final String COLUMN_ARTIST_NAME = "name";
		static final String COLUMN_ARTIST_TWITTER = "twitter";
		static final String COLUMN_ARTIST_TOP_CHART_COUNTRIES = "countries";
	}

	class TracksTable {
		static final String TRACKS_TABLE_NAME = "tracks";

		static final String COLUMN_TRACK_ID = "trackId";
		static final String COLUMN_TRACK_NAME = "name";
		static final String COLUMN_TRACK_ALBUM = "album";
		static final String COLUMN_TRACK_ARTIST_ID = "artistId";
		static final String COLUMN_TRACK_ALBUM_COVER = "albumCover";
	}
}
