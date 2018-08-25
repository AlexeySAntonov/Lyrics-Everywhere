package com.aleksejantonov.lyricseverywhere.ui.settings

import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_settings, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    toolbar.apply {
      title = getText(R.string.settings)
      setNavigationOnClickListener { activity.setDrawerState() }
    }

    val mode = AppCompatDelegate.getDefaultNightMode()
    when (mode) {
      -1 -> followSysButton.isChecked = true
      0  -> autoButton.isChecked = true
      1  -> dayButton.isChecked = true
      2  -> nightButton.isChecked = true
    }

    followSysButton.setOnClickListener { setMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) }
    autoButton.setOnClickListener { setMode(AppCompatDelegate.MODE_NIGHT_AUTO) }
    nightButton.setOnClickListener { setMode(AppCompatDelegate.MODE_NIGHT_YES) }
    dayButton.setOnClickListener { setMode(AppCompatDelegate.MODE_NIGHT_NO) }
  }

  private fun setMode(@AppCompatDelegate.NightMode mode: Int) {
    AppCompatDelegate.setDefaultNightMode(mode)
    activity.recreate()
  }
}