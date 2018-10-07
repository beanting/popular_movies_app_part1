package com.vincentangway.popularmovies.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vincentangway.popularmovies.R;
import com.vincentangway.popularmovies.data.model.Movies;
import com.vincentangway.popularmovies.data.realm.RealmObjectMovie;
import com.vincentangway.popularmovies.ui.adapter.MoviesAdapter;
import com.vincentangway.popularmovies.ui.helper.RetrofitClients;
import com.vincentangway.popularmovies.utils.MovieUtils;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesFragment extends Fragment {

    private static final int MOVIE_POPULAR_LIST = 0;
    private static final int MOVIE_TOP_RATED_LIST = 1;
    Call<Movies> popularMovies;
    Call<Movies> topRatedMovies;
    RetrofitClients.AllMoviesClient moviesData;
    Realm realm;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView moviesRecyclerView;
    MoviesAdapter moviesRecyclerAdapter;
    final String DATA_LOG_TAG = "Retrofit Log";
    int listType;
    String apiKey;

    public MoviesFragment() {}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu_activity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sort) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MenuDialogStyle);
            final String[] sortTypes = getResources().getStringArray(R.array.sort_types);
            builder.setSingleChoiceItems(sortTypes, listType, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listType = which;
                    changeListType(which);
                    dialog.dismiss();
                }
            });
            builder.setCancelable(true);
            builder.setTitle(R.string.sort_dialog_title);
            builder.show();
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        realm = Realm.getDefaultInstance();
        apiKey = getString(R.string.api_key);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        listType = sharedPref.getInt(getString(R.string.pref_sort_type_key), MOVIE_POPULAR_LIST);

        initViews(rootView);
        getMovies();
        getData();
        showLoading();
        setSwipeRefresh();

        return rootView;
    }

    private void initViews(View rootView) {
        moviesRecyclerView = rootView.findViewById(R.id.movies_recycler);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
    }

    private void getMovies() {
        final String BASE_URL = "http://api.themoviedb.org/3/";
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        moviesData = retrofit.create(RetrofitClients.AllMoviesClient.class);

        popularMovies = moviesData.getPopularMovies(apiKey);
        topRatedMovies = moviesData.getTopRatedMovies(apiKey);

        popularMovies.enqueue(MovieUtils.getPopularMovies(realm, DATA_LOG_TAG, MOVIE_POPULAR_LIST, swipeRefreshLayout));
        topRatedMovies.enqueue(MovieUtils.getTopRatedMovies(realm, DATA_LOG_TAG, MOVIE_TOP_RATED_LIST, swipeRefreshLayout));
    }

    private void getData() {
        moviesRecyclerAdapter = new MoviesAdapter(getActivity(), realm.where(RealmObjectMovie.class).equalTo("listType", listType).findAll());
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (isTablet) {
                moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            } else {
                moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            }
        } else {
            if (isTablet) {
                moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            } else {
                moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            }
        }
        moviesRecyclerView.setAdapter(moviesRecyclerAdapter);
    }

    private void showLoading() {
        if (moviesRecyclerAdapter.getData() == null)
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        else if (moviesRecyclerAdapter.getData().size() == 0)
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
    }

    private void setSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMovies();
            }
        });
    }

    private void changeListType(int which) {
        listType = which;

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.pref_sort_type_key), listType);
        editor.apply();

        getData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        if (popularMovies != null) {
            popularMovies.cancel();
            popularMovies = null;
        }
        if (topRatedMovies != null) {
            topRatedMovies.cancel();
            topRatedMovies = null;
        }
    }
}
