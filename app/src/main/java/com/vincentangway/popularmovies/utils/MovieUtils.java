package com.vincentangway.popularmovies.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.vincentangway.popularmovies.data.model.Movie;
import com.vincentangway.popularmovies.data.model.Movies;
import com.vincentangway.popularmovies.data.realm.RealmObjectMovie;

import java.util.List;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieUtils {
    public static long getNextMovieUniqID(Realm realm) {
        Number number = realm.where(RealmObjectMovie.class).max("uniqID");
        if (number == null) return 1;
        else return (long) number + 1;
    }

    public static Callback<Movies> getPopularMovies(final Realm realm
            , final String DATA_LOG_TAG
            , final int listType
            , final SwipeRefreshLayout swipeRefreshLayout) {
        return new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    Log.d(DATA_LOG_TAG, "success");
                    List<Movie> movies = response.body().getMovies();
                    MovieUtils.addMoviesToRealm(movies, listType, realm);
                } else
                    Log.d(DATA_LOG_TAG, response.message());
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(DATA_LOG_TAG, "failed");
            }
        };
    }

    public static Callback<Movies> getTopRatedMovies(final Realm realm
            , final String DATA_LOG_TAG
            , final int listType
            , final SwipeRefreshLayout swipeRefreshLayout) {
        return new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    Log.d(DATA_LOG_TAG, "success");
                    List<Movie> movies = response.body().getMovies();
                    MovieUtils.addMoviesToRealm(movies, listType, realm);
                } else
                    Log.d(DATA_LOG_TAG, response.message());
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(DATA_LOG_TAG, "failed");
            }
        };
    }

    public static void addMoviesToRealm(List<Movie> movies, int listType, Realm realm) {
        for (Movie movie : movies) {
            final String title = movie.getTitle();
            final String posterPath = movie.getPosterPath();
            final String backdropPath = movie.getBackdropPath();
            final int id = movie.getId();
            final double popularity = movie.getPopularity();
            final double voteAverage = movie.getVoteAverage();
            final String overview = movie.getOverview();
            final String releaseDate = movie.getReleaseDate();
            realm.beginTransaction();
            RealmObjectMovie realmObjectMovie = realm.where(RealmObjectMovie.class)
                    .equalTo("id", id).equalTo("listType", listType).findFirst();
            if (realmObjectMovie == null) {
                realmObjectMovie = realm.createObject(RealmObjectMovie.class, MovieUtils.getNextMovieUniqID(realm));
                realmObjectMovie.setFavorite(false);
                realmObjectMovie.setListType(listType);
            }
            realmObjectMovie.setTitle(title);
            realmObjectMovie.setPosterPath(posterPath);
            realmObjectMovie.setBackdropPath(backdropPath);
            realmObjectMovie.setId(id);
            realmObjectMovie.setPopularity(popularity);
            realmObjectMovie.setVoteAverage(voteAverage);
            realmObjectMovie.setOverview(overview);
            realmObjectMovie.setReleaseDate(releaseDate);
            realm.copyToRealmOrUpdate(realmObjectMovie);
            realm.commitTransaction();
        }
    }
}
