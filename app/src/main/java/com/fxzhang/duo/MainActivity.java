package com.fxzhang.duo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fxzhang.duo.service.MyRetrofitBuilder;
import com.fxzhang.duo.service.RiotGamesService;

import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    protected static final String PROFILE_ICON_URL = "http://ddragon.leagueoflegends.com/cdn/6.24.1/img/profileicon/@@@.png";
    protected static final String CHAMPION_ICON_URL = "http://ddragon.leagueoflegends.com/cdn/7.2.1/img/champion/@@@.png";

    protected RiotGamesService riotGamesService;
    protected RiotGamesService riotGamesServiceForMatches;
    private ViewGroup mMainView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mMainView = (ViewGroup) findViewById(R.id.main_content_view);

        Retrofit retrofit = MyRetrofitBuilder.getRetrofit();
        riotGamesService = retrofit.create(RiotGamesService.class);

        Retrofit retrofitForMatches = MyRetrofitBuilder.getRetrofitForMatches();
        riotGamesServiceForMatches = retrofitForMatches.create(RiotGamesService.class);
    }

    protected void setContentMainView(int id) {
        LayoutInflater mInflater = getLayoutInflater();
        View view = mInflater.inflate(id, mMainView, false);
        mMainView.removeAllViews();
        mMainView.addView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}
