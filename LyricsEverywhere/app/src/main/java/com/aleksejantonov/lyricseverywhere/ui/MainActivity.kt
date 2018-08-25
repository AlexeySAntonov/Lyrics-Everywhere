package com.aleksejantonov.lyricseverywhere.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import com.aleksejantonov.lyricseverywhere.R.drawable
import com.aleksejantonov.lyricseverywhere.R.id
import com.aleksejantonov.lyricseverywhere.R.layout
import com.aleksejantonov.lyricseverywhere.di.DI
import com.aleksejantonov.lyricseverywhere.ui.Base.BaseFragment
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType.GB
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType.RU
import com.aleksejantonov.lyricseverywhere.ui.Base.QueryType.US
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType.COUNTRY
import com.aleksejantonov.lyricseverywhere.ui.Base.ScreenType.SETTINGS
import com.aleksejantonov.lyricseverywhere.ui.login_screen.LoginActivity
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  companion object {
    private const val DRAWER_ITEM_ID = "drawer_item_id"
    fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
  }

  private var lastItem: MenuItem? = null
  private var lastItemId = id.rus

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    navigationView.apply {
      setNavigationItemSelectedListener(this@MainActivity)
      itemIconTintList = null
    }
    savedInstanceState?.getInt(DRAWER_ITEM_ID)?.let {
      lastItemId = it
      defaultInit(it)
    } ?: defaultInit(lastItemId)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(DRAWER_ITEM_ID, lastItemId)
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

  private fun defaultInit(lastItemId: Int?) =
      supportFragmentManager
          .findFragmentById(id.fragmentContainer)?.let {
            lastItemId?.let {
              lastItem = navigationView.menu.findItem(it)
              if (it != id.settings && it != id.logOut) lastItem?.setIcon(drawable.ic_star_gold_24dp)
            }
          }
          ?: onNavigationItemSelected(navigationView.menu.getItem(0))

  private fun selectDrawerItem(item: MenuItem) {

    if (lastItem != null && lastItem!!.itemId != id.settings) lastItem!!.setIcon(drawable.ic_star_white_24dp)

    lastItem = item
    lastItemId = item.itemId
    val country = when (item.itemId) {
      id.usa -> US
      id.gb  -> GB
      else   -> RU
    }

    when (item.itemId) {
      id.settings -> navigateTo(SETTINGS, QueryType.SETTINGS)
      id.logOut   -> {
        DI.componentManager().appComponent.preferences.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
      }
    }

    if (item.itemId != id.logOut && item.itemId != id.settings) {
      navigateTo(COUNTRY, country)
      item.setIcon(drawable.ic_star_gold_24dp)
    }
    drawerLayout.closeDrawers()
  }

  fun navigateTo(screen: ScreenType, queryType: QueryType, addToBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction()
        .replace(id.fragmentContainer, BaseFragment.newInstance(screen, queryType))
        .apply { if (addToBackStack) addToBackStack(null) }
        .commitAllowingStateLoss()
  }
}
