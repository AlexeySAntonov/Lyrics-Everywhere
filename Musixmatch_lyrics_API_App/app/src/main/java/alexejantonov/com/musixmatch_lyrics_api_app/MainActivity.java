package alexejantonov.com.musixmatch_lyrics_api_app;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks.ArtistsAndTracksListFragment;

public class MainActivity extends AppCompatActivity {

	private FragmentManager fragmentManager;

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private NavigationView navigationView;
	private Toolbar toolbar;
	private MenuItem lastDrawerMenuItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		drawerLayout = findViewById(R.id.drawerLayout);
		navigationView = findViewById(R.id.navigationView);
		navigationView.setNavigationItemSelectedListener(item -> {
			selectDrawerItem(item);
			return true;
		});
		navigationView.setItemIconTintList(null);

		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
		toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

		drawerToggle = setupDrawerToggle();
		drawerLayout.addDrawerListener(drawerToggle);

		defaultInit();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d("Activity", "onStart()");
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("Activity", "onResume()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("Activity", "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("Activity", "onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d("Activity", "onDestroy()");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.search_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.search);
		searchItem.collapseActionView();

		final SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setQueryHint(getString(R.string.search_by_artist_name));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				// Perform query
				Log.d("Query: ", query);
				fragmentManager
						.beginTransaction()
						.replace(R.id.fragmentContainer, ArtistsAndTracksListFragment.newInstance(null, query))
						.addToBackStack(null)
						.commit();

				searchItem.collapseActionView();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	private void defaultInit() {
		fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
		if (fragment == null) {
			fragment = ArtistsAndTracksListFragment.newInstance("ru");
			fragmentManager
					.beginTransaction()
					.add(R.id.fragmentContainer, fragment)
					.addToBackStack(null)
					.commit();
		}
	}

	private void selectDrawerItem(MenuItem item) {

		if (lastDrawerMenuItem != null && lastDrawerMenuItem.getItemId() != R.id.settings) {
			lastDrawerMenuItem.setIcon(R.drawable.ic_star_white_24dp);
		}

		lastDrawerMenuItem = item;
		String country = "";

		switch (item.getItemId()) {
			case R.id.rus:
				Snackbar.make(navigationView, R.string.russian_top_chart, Snackbar.LENGTH_LONG).show();
				country = "ru";
				break;
			case R.id.usa:
				Snackbar.make(navigationView, R.string.usa_top_chart, Snackbar.LENGTH_LONG).show();
				country = "us";
				break;
			case R.id.gb:
				Snackbar.make(navigationView, R.string.britain_top_chart, Snackbar.LENGTH_LONG).show();
				country = "gb";
				break;
			case R.id.settings:
				Snackbar.make(navigationView, R.string.settings, Snackbar.LENGTH_LONG).show();
				break;
			case R.id.exit:
				this.finish();
				break;
		}

		if (item.getItemId() != R.id.exit && item.getItemId() != R.id.settings) {
			fragmentManager
					.beginTransaction()
					.replace(R.id.fragmentContainer, ArtistsAndTracksListFragment.newInstance(country))
					.addToBackStack(null)
					.commit();
			item.setIcon(R.drawable.ic_star_gold_24dp);
			drawerLayout.closeDrawers();
		}
	}

	private ActionBarDrawerToggle setupDrawerToggle() {
		return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
	}
}
