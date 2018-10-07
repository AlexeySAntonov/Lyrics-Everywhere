package com.aleksejantonov.lyricseverywhere.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.app.AppCompatDelegate.*
import android.view.View
import com.aleksejantonov.lyricseverywhere.R
import com.aleksejantonov.lyricseverywhere.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.autoButton
import kotlinx.android.synthetic.main.fragment_settings.dayButton
import kotlinx.android.synthetic.main.fragment_settings.followSysButton
import kotlinx.android.synthetic.main.fragment_settings.nightButton
import kotlinx.android.synthetic.main.fragment_settings.toolbar

class SettingsFragment : BaseFragment() {
  companion object {
    fun newInstance() = SettingsFragment()
  }

  override val layoutRes = R.layout.fragment_settings

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    toolbar.apply {
      title = getText(R.string.settings)
      setNavigationOnClickListener { activity.toggleDrawer() }
    }

    val mode = AppCompatDelegate.getDefaultNightMode()
    when (mode) {
      MODE_NIGHT_FOLLOW_SYSTEM -> followSysButton.isChecked = true
      MODE_NIGHT_AUTO          -> autoButton.isChecked = true
      MODE_NIGHT_NO            -> dayButton.isChecked = true
      MODE_NIGHT_YES           -> nightButton.isChecked = true
    }

    followSysButton.setOnClickListener { setMode(MODE_NIGHT_FOLLOW_SYSTEM) }
    autoButton.setOnClickListener { setMode(MODE_NIGHT_AUTO) }
    nightButton.setOnClickListener { setMode(MODE_NIGHT_YES) }
    dayButton.setOnClickListener { setMode(MODE_NIGHT_NO) }
  }

  private fun setMode(@AppCompatDelegate.NightMode mode: Int) {
    AppCompatDelegate.setDefaultNightMode(mode)
    activity.recreate()
  }
}