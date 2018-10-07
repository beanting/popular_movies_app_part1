package com.vincentangway.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.ui.fragment.MoviesDetailsFragment;

public class ActivityDetails extends AppCompatActivity {

    private static final String MOVIE_FRAGMENT_TAG = "fragment_movie";
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle("");
        }

        if (savedInstanceState == null) {
            MoviesDetailsFragment fragment = new MoviesDetailsFragment();
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details_container, fragment, MOVIE_FRAGMENT_TAG)
                    .commit();
        }
    }
}
