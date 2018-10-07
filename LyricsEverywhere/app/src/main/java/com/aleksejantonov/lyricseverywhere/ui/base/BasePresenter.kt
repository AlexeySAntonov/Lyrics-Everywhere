package com.aleksejantonov.lyricseverywhere.ui.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V : MvpView> : MvpPresenter<V>() {
  private val disposables = CompositeDisposable()
  val viewActions = PublishRelay.create<Any>()

  protected fun Disposable.keepUntilDestroy() {
    disposables.add(this)
  }

  override fun onDestroy() {
    disposables.dispose()
    super.onDestroy()
  }
}
