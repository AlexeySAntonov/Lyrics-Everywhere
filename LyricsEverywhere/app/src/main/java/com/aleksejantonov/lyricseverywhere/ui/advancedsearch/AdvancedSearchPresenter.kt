package com.aleksejantonov.lyricseverywhere.ui.advancedsearch

import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.api.config.Constants
import com.aleksejantonov.lyricseverywhere.sl.SL
import com.aleksejantonov.lyricseverywhere.ui.base.BasePresenter
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnSearchClick
import com.aleksejantonov.lyricseverywhere.ui.base.BaseView.Action.OnTrackClick
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.entity.LoadingItem
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.entity.PlaceHolderItem
import com.aleksejantonov.lyricseverywhere.utils.DataContainersUtil
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

@InjectViewState
class AdvancedSearchPresenter : BasePresenter<AdvancedSearchView>() {
  private val musixMatchService = SL.componentManager().appComponent.service
  private val preferences = SL.componentManager().appComponent.preferences

  override fun onFirstViewAttach() {
    viewState.showItems(listOf(PlaceHolderItem(R.drawable.ic_cloud_blue_128dp)))

    viewActions
        .subscribeBy { handleAction(it as Action) }
        .keepUntilDestroy()
  }

  private fun handleAction(action: Action) {
    when (action) {
      is OnSearchClick -> searchRequest(action.query)
      is OnTrackClick  -> viewState.showTrackDetails(action.track, action.sharedImageView)
    }
  }

  @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
  private fun searchRequest(query: String) {
    musixMatchService
        .trackSearch(preferences.getString(Constants.API_KEY, ""), query)
        .map { DataContainersUtil.trackContainersToTracks(it.message.body.trackContainers) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe { viewState.showItems(listOf(LoadingItem())) }
        .subscribe(viewState::showItems, Timber::e)
        .keepUntilDestroy()
  }
}