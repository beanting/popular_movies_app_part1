package com.vincentangway.popularmovies.ui.helper;

import com.vincentangway.popularmovies.data.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RetrofitClients {
    public interface AllMoviesClient {

        String popularEndPoint = "movie/popular";
        String topRatedEndPoint = "movie/top_rated";

        @GET(popularEndPoint)
        Call<Movies> getPopularMovies(
                @Query("api_key") String apiKey
        );

        @GET(topRatedEndPoint)
        Call<Movies> getTopRatedMovies(
                @Query("api_key") String apiKey
        );
    }
}
