package com.aleksejantonov.lyricseverywhere.utils

import com.aleksejantonov.lyricseverywhere.api.entities.album.Album
import com.aleksejantonov.lyricseverywhere.api.entities.album.AlbumContainer
import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.artist.ArtistContainer
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.api.entities.track.TrackContainer

object DataContainersUtil {

  fun artistContainersToArtists(artistContainers: List<ArtistContainer>, country: String): List<Artist> =
      artistContainers.map { it.artist.apply { topChartCountries = country } }

  fun trackContainersToTracks(trackContainers: List<TrackContainer>): List<Track> =
      trackContainers.map { it.track }

  fun albumContainersToAlbums(albumContainers: List<AlbumContainer>): List<Album> =
      albumContainers.map { it.album }
}
