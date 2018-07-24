package alexejantonov.com.musixmatch_lyrics_api_app

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType.GB
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType.RU
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType.US
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.ScreenType
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.ScreenType.COUNTRY
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.ScreenType.SETTINGS
import alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen.LoginActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  companion object {
    fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
  }

  private var lastDrawerMenuItem: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    navigationView.apply {
      setNavigationItemSelectedListener(this@MainActivity)
      itemIconTintList = null
    }
    defaultInit()
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.closeDrawer(Gravity.START)
    else super.onBackPressed()
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    selectDrawerItem(item)
    return true
  }

  fun setDrawerState() {
    if (drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.closeDrawer(Gravity.START)
    else drawerLayout.openDrawer(Gravity.START)
  }

  private fun defaultInit() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
      ?: onNavigationItemSelected(navigationView.menu.getItem(0))

  private fun selectDrawerItem(item: MenuItem) {

    if (lastDrawerMenuItem != null && lastDrawerMenuItem!!.itemId != R.id.settings) {
      lastDrawerMenuItem!!.setIcon(R.drawable.ic_star_white_24dp)
    }

    lastDrawerMenuItem = item
    val country = when (item.itemId) {
      R.id.usa -> US
      R.id.gb  -> GB
      else     -> RU
    }

    when (item.itemId) {
      R.id.settings -> navigateTo(SETTINGS, QueryType.SETTINGS)
      R.id.logOut   -> {
        MyApplication.preferences.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
      }
    }

    if (item.itemId != R.id.logOut && item.itemId != R.id.settings) {
      navigateTo(COUNTRY, country)
      item.setIcon(R.drawable.ic_star_gold_24dp)
    }
    drawerLayout.closeDrawers()
  }

  fun navigateTo(screen: ScreenType, queryType: QueryType, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragmentContainer, BaseFragment.newInstance(screen, queryType))
        .apply { if (addToBackStack) addToBackStack(null) }
        .commitAllowingStateLoss()
  }
}
