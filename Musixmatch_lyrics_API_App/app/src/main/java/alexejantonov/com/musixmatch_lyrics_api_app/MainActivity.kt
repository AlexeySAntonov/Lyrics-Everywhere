package alexejantonov.com.musixmatch_lyrics_api_app

import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.BaseFragment
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.FragmentType.COUNTRY
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.FragmentType.SEARCH
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType.GB
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType.RU
import alexejantonov.com.musixmatch_lyrics_api_app.ui.Base.QueryType.US
import alexejantonov.com.musixmatch_lyrics_api_app.ui.login_screen.LoginActivity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navigationView
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
  companion object {
    fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
  }

  private var drawerToggle: ActionBarDrawerToggle? = null
  private var lastDrawerMenuItem: MenuItem? = null
  var searchView: SearchView? = null
  var searchItem: MenuItem? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    navigationView.apply {
      setNavigationItemSelectedListener(this@MainActivity)
      itemIconTintList = null
    }
    setSupportActionBar(toolbar)
    toolbar.apply {
      setNavigationIcon(R.drawable.ic_menu_white_24dp)
      setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }
    drawerToggle = setupDrawerToggle()
    drawerLayout.addDrawerListener(drawerToggle as ActionBarDrawerToggle)
    defaultInit()
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    drawerToggle?.syncState()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    drawerToggle?.onConfigurationChanged(newConfig)
  }

  override fun onOptionsItemSelected(item: MenuItem) =
      drawerToggle?.onOptionsItemSelected(item) ?: super.onOptionsItemSelected(item)

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.search_menu, menu)

    searchItem = menu.findItem(R.id.search)
    searchItem?.setOnMenuItemClickListener {
      supportFragmentManager.beginTransaction()
          .replace(R.id.fragmentContainer, BaseFragment.newInstance(SEARCH, QueryType.SEARCH))
          .addToBackStack(null)
          .commitAllowingStateLoss()
      false
    }
    searchView = searchItem?.actionView as SearchView
    searchView?.apply {
      queryHint = getString(R.string.search_by_artist_name)
      isSubmitButtonEnabled = true
      setIconifiedByDefault(false)
    }
    return super.onCreateOptionsMenu(menu)
  }

  override fun onBackPressed() {
    if (drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.closeDrawer(Gravity.START)
    else super.onBackPressed()
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    selectDrawerItem(item)
    return true
  }

  private fun defaultInit() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
      ?: onNavigationItemSelected(navigationView.menu.getItem(0))

  private fun selectDrawerItem(item: MenuItem) {

    if (lastDrawerMenuItem != null && lastDrawerMenuItem!!.itemId != R.id.settings) {
      lastDrawerMenuItem!!.setIcon(R.drawable.ic_star_white_24dp)
    }

    lastDrawerMenuItem = item
    var countryId: QueryType? = null

    when (item.itemId) {
      R.id.rus      -> countryId = RU
      R.id.usa      -> countryId = US
      R.id.gb       -> countryId = GB
      R.id.settings -> Snackbar.make(navigationView, R.string.settings, Snackbar.LENGTH_LONG).show()
      R.id.logOut   -> {
        MyApplication.preferences?.let { it.edit().clear().apply() }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
      }
    }

    if (item.itemId != R.id.logOut && item.itemId != R.id.settings) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.fragmentContainer, BaseFragment.newInstance(COUNTRY, countryId))
          .commitAllowingStateLoss()
      item.setIcon(R.drawable.ic_star_gold_24dp)
    }
    drawerLayout.closeDrawers()
  }

  private fun setupDrawerToggle() =
      ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
}
