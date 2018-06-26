package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base

//Маркер для слияния списков
interface BaseData {

  var artistId: Int

  override fun equals(other: Any?): Boolean
}
