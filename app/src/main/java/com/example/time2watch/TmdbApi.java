package com.example.time2watch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbApi {
    String API_KEY = "8aa079a5f18d00f15079b7d3c90dc10a";

    @GET("discover/movie")
    Call<MovieResponse> getMoviesByGenre(
            @Query("api_key") String apiKey,
            @Query("with_genres") String genreIds,
            @Query("sort_by") String sortBy,
            @Query("page") int page
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

}