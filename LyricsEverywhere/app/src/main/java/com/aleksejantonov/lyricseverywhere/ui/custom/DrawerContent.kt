package com.aleksejantonov.lyricseverywhere.ui.custom

import android.content.Context
import android.support.constraint.motion.MotionLayout
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.DrawerLayout.DrawerListener
import android.util.AttributeSet
import android.view.View

class DrawerContent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), DrawerListener {

  override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    progress = slideOffset
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    (parent as? DrawerLayout)?.addDrawerListener(this)
  }

  override fun onDrawerStateChanged(p0: Int) = Unit
  override fun onDrawerClosed(p0: View) = Unit
  override fun onDrawerOpened(p0: View) = Unit
}