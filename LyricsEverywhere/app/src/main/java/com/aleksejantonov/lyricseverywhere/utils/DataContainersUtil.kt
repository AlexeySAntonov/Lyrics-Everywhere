package com.aleksejantonov.lyricseverywhere.utils

import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.artist.ArtistContainer
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.api.entities.track.TrackContainer

object DataContainersUtil {

  fun artistContainersToArtists(artistContainers: List<ArtistContainer>, country: String): List<Artist> {
    val artists = mutableListOf<Artist>()
    for (i in artistContainers.indices) {
      artists.add(artistContainers[i].artist)
    }

    for (artist in artists) {
      artist.topChartCountries = artist.topChartCountries + country
    }
    return artists
  }

  fun trackContainersToTracks(trackContainers: List<TrackContainer>): List<Track> {
    val tracks = mutableListOf<Track>()
    for (i in trackContainers.indices) {
      tracks.add(trackContainers[i].track)
    }
    return tracks
  }
}
