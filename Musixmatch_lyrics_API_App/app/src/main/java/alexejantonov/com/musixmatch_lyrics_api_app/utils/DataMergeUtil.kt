package alexejantonov.com.musixmatch_lyrics_api_app.utils

import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseData

object DataMergeUtil {

  fun listsMerge(artists: List<Artist>, tracks: List<Track>): List<BaseData> {
    val data = mutableListOf<BaseData>()

    for (i in artists.indices) {
      data.add(artists[i])
      for (j in tracks.indices) {
        if (artists[i].artistId == tracks[j].artistId) {
          data.add(tracks[j])
        }
      }
    }
    return data
  }

  fun searchListsMerge(artists: List<Artist>, tracks: List<Track>, data: List<BaseData>): List<BaseData> {
    val mergeData = mutableListOf<BaseData>()
    val dataMap = mutableMapOf<Artist, MutableList<Track>>()

    for (i in data.indices) {
      if (data[i] is Artist) {
        for (j in tracks.indices) {
          if (data[i].artistId == tracks[j].artistId) {
            if (dataMap.containsKey(data[i])) {
              dataMap[data[i]]?.add(tracks[j])
            } else {
              val mapBucketTracks = ArrayList<Track>()
              mapBucketTracks.add(tracks[j])
              dataMap[data[i] as Artist] = mapBucketTracks
            }
          }
        }
      } else if (data[i] is Track) {
        for (j in artists.indices) {
          if (data[i].artistId == artists[j].artistId) {
            if (dataMap.containsKey(artists[j])) {
              dataMap[artists[j]]?.add(data[i] as Track)
              break
            } else {
              val mapBucketTrack = ArrayList<Track>()
              mapBucketTrack.add(data[i] as Track)
              dataMap[artists[j]] = mapBucketTrack
              break
            }
          }
        }
      }
    }

    for (artist in dataMap.keys) {
      mergeData.add(artist)
      mergeData.addAll(dataMap[artist] as List<BaseData>)
    }

    return mergeData
  }
}
