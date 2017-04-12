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
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunkka.gank.MainContract.MainView;
import com.kunkka.gank.pojo.Type;
import com.kunkka.gank.tools.Utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MainAdapter mMainAdapter;
    private Toast mToast;
    private long mLastBackTime = 0;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        MainContract.Presenter presenter = new MainPresenter();
        drawer.addDrawerListener(presenter.getDrawerListener());
        toggle.syncState();

        ItemsFragment fragment = ItemsFragment.newInstance(presenter);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_main, fragment);
        ft.commit();

        presenter.setItemView(fragment);
        presenter.setMainView(this);
        presenter.start();
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

    @Override
    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void initMenu(Collection<Type> types, OnCheckedChangeListener listener) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu().findItem(R.id.subscribe).getSubMenu();
        for (Type type : Type.getTypeMap().values()) {
            Log.d(TAG, type.toString());
            menu.add(Menu.NONE, type.getOrder(), type.getOrder(), type.getName());
            CheckBox box = new CheckBox(this);
            box.setTag(type.getName());
            box.setChecked(type.isEnable());
            box.setOnCheckedChangeListener(listener);
            menu.findItem(type.getOrder()).setActionView(box);
            menu.findItem(type.getOrder()).setIcon(type.getLogoResId(this));
        }
    }
}
