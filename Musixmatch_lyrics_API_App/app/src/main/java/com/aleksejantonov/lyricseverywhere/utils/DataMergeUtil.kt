package com.aleksejantonov.lyricseverywhere.utils

import com.aleksejantonov.lyricseverywhere.api.entities.artist.Artist
import com.aleksejantonov.lyricseverywhere.api.entities.track.Track
import com.aleksejantonov.lyricseverywhere.ui.Base.BaseData

object DataMergeUtil {

  fun listsMerge(artists: List<Artist>, tracks: List<Track>): List<BaseData> {
    val data = mutableListOf<BaseData>()

    for (artist in artists) {
      data.add(artist)
      for (track in tracks) {
        if (artist.artistId == track.artistId) {
          data.add(track)
        }
      }
    }
    return data
  }

  fun searchListsMerge(artists: List<Artist>, tracks: MutableList<Track>, query: String): List<BaseData> {
    val mergeData = mutableListOf<BaseData>()
    val tracksClone = mutableListOf<Track>().apply { addAll(tracks) }

    for (artist in artists) {
      if (artist.artistName.toLowerCase().contains(query.toLowerCase())) {
        mergeData.add(artist)
        for (track in tracks) {
          if (track.artistId == artist.artistId) {
            mergeData.add(track)
            tracksClone.remove(track)
          }
        }
      }
    }

    for (track in tracksClone) {
      if (track.trackName.toLowerCase().contains(query.toLowerCase())) {
        for (artist in artists) {
          if (artist.artistId == track.artistId) {
            if (mergeData.contains(artist).not()) {
              mergeData.add(artist)
            }
          }
        }
        mergeData.add(track)
      }
    }
    return mergeData
  }
}
