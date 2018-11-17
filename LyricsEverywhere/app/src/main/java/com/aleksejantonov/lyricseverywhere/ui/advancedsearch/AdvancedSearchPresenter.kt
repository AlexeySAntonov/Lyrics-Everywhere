package com.aleksejantonov.lyricseverywhere.ui.advancedsearch

import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.ui.base.delegate.entity.PlaceHolderItem
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

@InjectViewState
class AdvancedSearchPresenter : MvpPresenter<AdvancedSearchView>() {

  override fun onFirstViewAttach() {
    viewState.showItems(listOf(PlaceHolderItem(R.drawable.ic_cloud_blue_128dp)))
  }
}