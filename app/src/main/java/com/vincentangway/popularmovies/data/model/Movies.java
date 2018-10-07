package com.vincentangway.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movies {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Movie> movies = new ArrayList<Movie>();
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    /**
     * @return page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return movies
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * @param movies
     */
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    /**
     * @return totalResults
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return total_pages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages

     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
