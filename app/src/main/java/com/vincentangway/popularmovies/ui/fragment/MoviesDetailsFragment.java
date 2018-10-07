package com.vincentangway.popularmovies.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.data.realm.RealmObjectMovie;
import com.vincentangway.popularmovies.ui.activity.MainActivity;

import io.realm.Realm;

public class MoviesDetailsFragment extends Fragment {

    Realm realm;
    RealmObjectMovie movie;
    ImageView posterImageView;
    ImageView backdropImageView;
    TextView titleTextView;
    TextView captionTextView;
    TextView overviewTextView;
    NestedScrollView parentScrollView;
    CoordinatorLayout parentCoordinator;

    int id;
    String apiKey;
    boolean mTwoPane;

    public MoviesDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realm = Realm.getDefaultInstance();

        apiKey = getString(R.string.api_key);

        mTwoPane = getResources().getBoolean(R.bool.isTabletLand);

        if (mTwoPane && savedInstanceState == null) {
            id = 0;
        } else {
            id = getActivity().getIntent().getIntExtra(MainActivity.MOVIE_ID, 0);
        }

        initViews(view);
        initMovie();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initMovie() {

        boolean loadingFailure = false;

        movie = realm.where(RealmObjectMovie.class)
                .equalTo("id", id).findFirst();

        if (movie == null) {
            if (!mTwoPane) {
                Toast.makeText(getActivity(), getString(R.string.error_getting_id), Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            loadingFailure = true;
        }

        if (loadingFailure) {
            hideViews();
        } else {
            DisplayMovieDetails();
        }
    }

    private void hideViews() {
        parentScrollView.setVisibility(View.INVISIBLE);
    }

    private void showViews() {
        parentScrollView.setVisibility(View.VISIBLE);
        parentScrollView.fullScroll(View.FOCUS_UP);
    }

    private void initViews(View rootView) {
        posterImageView = (ImageView) rootView.findViewById(R.id.details_poster_image_view);
        backdropImageView = (ImageView) rootView.findViewById(R.id.details_backdrop_image_view);
        titleTextView = (TextView) rootView.findViewById(R.id.details_title_text_view);
        captionTextView = (TextView) rootView.findViewById(R.id.details_caption_text_view);
        overviewTextView = (TextView) rootView.findViewById(R.id.details_overview_text_view);
        parentScrollView = (NestedScrollView) rootView.findViewById(R.id.details_parent);
        parentCoordinator = (CoordinatorLayout) rootView.findViewById(R.id.parent_coordinator_details);
    }

    private void DisplayMovieDetails() {

        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getOverview());

        String caption = String.format(getString(R.string.details_caption_format)
                , movie.getReleaseDate()
                , String.valueOf(movie.getVoteAverage()));

        captionTextView.setText(caption);

        String backdropURL = String.format(getString(R.string.image_url_format_original)
                , movie.getBackdropPath());

        String posterURL = String.format(getString(R.string.image_url_format_w300)
                , movie.getPosterPath());

        Picasso.with(getActivity())
                .load(posterURL)
                .placeholder(R.drawable.poster_placeholder_small)
                .fit()
                .centerCrop()
                .into(posterImageView);

        Picasso.with(getActivity())
                .load(backdropURL)
                .placeholder(R.drawable.backdrop_placeholder)
                .fit()
                .centerCrop()
                .into(backdropImageView);
    }

    public void twoPaneDisplay(Integer id) {
        this.id = id;
        showViews();
        initMovie();
    }
}
