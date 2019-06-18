package com.abc.ytoddler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.abc.ytoddler.helpers.LocaleHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ProfileActivity";
    private ViewPager viewPager = null;
    private ActionBarDrawerToggle mToggle;
    MaterialSearchView searchView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Init Paper first
        Paper.init(this);

        //Default language is English
        String language = Paper.book().read("language");
        if(language == null)
            Paper.book().write("language", "en");

        updateView((String)Paper.book().read("language"));


        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);

        searchView = findViewById(R.id.search_view);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager =  findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.channel));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.playlist));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.live));

        final PagerAdapter adapter = new com.abc.ytoddler.adapters.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout mDrawerLayout = findViewById(R.id.drawert);
        mToggle=new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        TextView bigClock = (TextView) navigationHeaderView.findViewById(R.id.bigClock);
    }

    private void updateView(String lang) {
        Context context = LocaleHelper.setLocale(this, lang);
        Resources resources = context.getResources();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuLogout) {
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        else if (item.getItemId() == R.id.menuTimer)
        {
            finish();
            startActivity(new Intent(this, TimerActivity.class));
        }
        else if (item.getItemId() == R.id.english)
        {
            Paper.book().write("language", "en");
            updateView(Paper.book().read("language"));
        }
        else if (item.getItemId() == R.id.polish)
        {
            Paper.book().write("language", "pl");
            updateView(Paper.book().read("language"));
        }
        else if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        if(item.getItemId() == R.id.girls_one_four_years_old){
            Intent intent = new Intent(this, FourGirlActivity.class);
            this.startActivity(intent);
        } else if (item.getItemId() == R.id.girls_four_seven_years_old){
            Intent intent = new Intent(this, SevenGirlActivity.class);
            this.startActivity(intent);
        } else if (item.getItemId() == R.id.girls_seven_ten_years_old){
            Intent intent = new Intent(this, TenGirlActivity.class);
            this.startActivity(intent);
        } else if (item.getItemId() == R.id.boys_one_four_years_old){
            Intent intent = new Intent(this, FourBoyActivity.class);
            this.startActivity(intent);
        } else if (item.getItemId() == R.id.boys_four_seven_years_old){
            Intent intent = new Intent(this, SevenBoyActivity.class);
            this.startActivity(intent);
        } else if (item.getItemId() == R.id.boys_seven_ten_years_old){
            Intent intent = new Intent(this, TenBoyActivity.class);
            this.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawert);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        Log.i(TAG, "Stopped service");
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            Log.i(TAG, "Countdown seconds remaining: " +  millisUntilFinished / 1000);
            String dateString = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle(dateString);

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View navigationHeaderView = navigationView.getHeaderView(0);
            TextView bigClock = (TextView) navigationHeaderView.findViewById(R.id.bigClock);
            bigClock.setText(dateString);
        }
    }
}
