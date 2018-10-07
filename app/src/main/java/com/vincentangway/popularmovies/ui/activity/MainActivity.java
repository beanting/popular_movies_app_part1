package com.vincentangway.popularmovies.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.ui.fragment.MoviesDetailsFragment;
import com.vincentangway.popularmovies.ui.fragment.MoviesFragment;

public final class MainActivity extends AppCompatActivity {

    boolean mTwoPane;
    public static final String MOVIE_ID = "movieID";
    MoviesFragment moviesFragment;
    MoviesDetailsFragment moviesDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTwoPane = getResources().getBoolean(R.bool.isTabletLand);
        if (mTwoPane) {
            moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_main_tag));
            moviesDetailsFragment = (MoviesDetailsFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_details_tag));
        }

        final Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
    }

    public void handleRecyclerClick(Integer id) {
        if (mTwoPane) {
            moviesDetailsFragment.twoPaneDisplay(id);
        } else {
            Intent intent = new Intent(this, ActivityDetails.class);
            intent.putExtra(MOVIE_ID, id);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTwoPane = getResources().getBoolean(R.bool.isTabletLand);
        if (mTwoPane) {
            moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_main_tag));
            moviesDetailsFragment = (MoviesDetailsFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.fragment_details_tag));
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
}
