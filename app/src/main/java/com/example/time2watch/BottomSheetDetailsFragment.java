package com.example.time2watch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomSheetDetailsFragment extends BottomSheetDialogFragment {

    private static final String MOVIE_KEY = "movie_key";
    private TextView title, description, releaseDate;
    private Button trailerButton;
    private ImageView posterImageView;

    public static BottomSheetDetailsFragment newInstance(Movie movie) {
        BottomSheetDetailsFragment fragment = new BottomSheetDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_KEY, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_details, container, false);

        title = rootView.findViewById(R.id.detail_title);
        description = rootView.findViewById(R.id.detail_synopsis);
        releaseDate = rootView.findViewById(R.id.detail_release_date);
        trailerButton = rootView.findViewById(R.id.btn_watch);
        posterImageView = rootView.findViewById(R.id.detail_poster);

        trailerButton.setVisibility(View.VISIBLE);
        trailerButton.setEnabled(false); // Désactiver initialement

        Movie movie = getArguments().getParcelable(MOVIE_KEY);

        if (movie != null) {
            title.setText(movie.getTitle());
            description.setText(movie.getOverview());
            releaseDate.setText(movie.getReleaseDate());

            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Glide.with(getContext())
                    .load(posterUrl)
                    .into(posterImageView);

            fetchTrailer(movie.getId());
        }

        trailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailerButton.getTag() != null) {
                    String videoUrl = (String) trailerButton.getTag();
                    if (videoUrl != null && !videoUrl.isEmpty()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoUrl));
                        startActivity(intent);
                    }
                }
            }
        });

        ImageButton closeButton = rootView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());

        return rootView;
    }

    private void fetchTrailer(int movieId) {
        Log.d("BottomSheetDetails", "Fetching trailer for movie ID: " + movieId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApi = retrofit.create(TmdbApi.class);

        // Appel API pour obtenir les vidéos (trailers)
        Call<TrailerResponse> call = tmdbApi.getMovieVideos(movieId, TmdbApi.API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, retrofit2.Response<TrailerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TrailerResponse trailerResponse = response.body();
                    Log.d("BottomSheetDetails", "Trailer count: " + trailerResponse.getResults().size());

                    if (!trailerResponse.getResults().isEmpty()) {
                        String trailerKey = trailerResponse.getResults().get(0).getKey();
                        trailerButton.setTag(trailerKey);
                        trailerButton.setEnabled(true);
                        Log.d("BottomSheetDetails", "Trailer found: " + trailerKey);
                    } else {
                        trailerButton.setEnabled(false);
                        Log.d("BottomSheetDetails", "No trailer found");
                    }
                } else {
                    trailerButton.setEnabled(false);
                    Log.e("BottomSheetDetails", "API Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e("BottomSheetDetails", "API Call Failed: " + t.getMessage(), t);
                trailerButton.setEnabled(false);
            }
        });
    }
}