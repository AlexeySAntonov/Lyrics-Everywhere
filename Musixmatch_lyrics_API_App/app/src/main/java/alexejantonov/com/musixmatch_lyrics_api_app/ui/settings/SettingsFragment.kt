package alexejantonov.com.musixmatch_lyrics_api_app.ui.settings

import alexejantonov.com.musixmatch_lyrics_api_app.R
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseFragment
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    nightButton.setOnClickListener {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
      activity.recreate()
    }
  }
}