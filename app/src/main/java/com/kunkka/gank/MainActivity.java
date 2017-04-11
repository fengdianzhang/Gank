package com.kunkka.gank;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunkka.gank.pojo.Type;
import com.kunkka.gank.tools.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainAdapter mMainAdapter;
    private Toast mToast;
    private long mLastBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initDrawerMenu();

        MainContract.Presenter presenter = new MainPresenter(toolbar);
        ItemsFragment fragment = ItemsFragment.newInstance(presenter);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_main, fragment);
        ft.commit();

        presenter.setView(fragment);
        presenter.start();
    }

    private void initDrawerMenu() {
        String type = Utils.loadJSONFromAsset(this, "types.json");
        List<Type> types = new Gson().fromJson(type, new TypeToken<List<Type>>() {
        }.getType());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu().findItem(R.id.subscribe).getSubMenu();
        for (Type t : types) {
            menu.add(Menu.NONE, t.getOrder(), t.getOrder(), t.getName());
            CheckBox box = new CheckBox(this);
            box.setTag(t.getName());
            box.setChecked(true);
            box.setOnCheckedChangeListener(this);
            menu.findItem(t.getOrder()).setActionView(box);
            menu.findItem(t.getOrder()).setIcon(t.getLogoResId(this));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (System.currentTimeMillis() - mLastBackTime < 2000) {
            if (mToast != null) {
                mToast.cancel();
            }
            super.onBackPressed();
        } else {
            mToast = Toast.makeText(this, R.string.exit_notice, Toast.LENGTH_SHORT);
            mToast.show();
            mLastBackTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//item.getActionView()
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d(TAG, "onNavigationItemSelected: " + item);
//        if (id == R.id.nav_camera) {
            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {

//        } else if (id == R.id.nav_slideshow) {

//        } else if (id == R.id.nav_manage) {

//        } else if (id == R.id.nav_share) {
//            NavigationView nav = (NavigationView)findViewById(R.id.navigation_view);
//            CompoundButton switchView = (CompoundButton) MenuItemCompat.getActionView(item);
//            switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { }
//            });
//        } else if (id == R.id.nav_send) {

//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "onCheckedChanged: " + buttonView.getTag());
    }
}
