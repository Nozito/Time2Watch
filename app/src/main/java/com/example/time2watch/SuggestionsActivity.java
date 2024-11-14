package com.example.time2watch;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SuggestionsActivity extends AppCompatActivity implements SuggestionsAdapter.OnMovieClickListener {

    private static final String TAG = "SuggestionsActivity";

    private RecyclerView recyclerView;
    private SuggestionsAdapter adapter;
    private TextView suggestionsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);

        recyclerView = findViewById(R.id.recycler_view_suggestions);
        suggestionsTitle = findViewById(R.id.suggestions_title);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String weatherCondition = getIntent().getStringExtra("weather_condition");
        if (weatherCondition == null || weatherCondition.isEmpty()) {
            weatherCondition = "clear";
        }
        Log.d(TAG, "Condition météo utilisée : " + weatherCondition);

        loadSuggestions(weatherCondition);
    }

    private void loadSuggestions(String weatherCondition) {
        String genreIds = getGenreByWeather(weatherCondition);
        Log.d(TAG, "Genre choisi pour la condition météo : " + genreIds);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbApi tmdbApi = retrofit.create(TmdbApi.class);

        Call<MovieResponse> call = tmdbApi.getMoviesByGenre(TmdbApi.API_KEY, genreIds, "popularity.desc", 1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    Log.d(TAG, "Nombre de films récupérés : " + (movies != null ? movies.size() : 0));

                    if (movies != null && !movies.isEmpty()) {
                        adapter = new SuggestionsAdapter(movies, SuggestionsActivity.this, SuggestionsActivity.this);
                        recyclerView.setAdapter(adapter);
                        suggestionsTitle.setText("Suggestions de films pour un temps " + weatherCondition);
                    } else {
                        showErrorToast("Aucun film trouvé pour cette météo");
                    }
                } else {
                    Log.e(TAG, "Réponse de l'API invalide ou vide");
                    showErrorToast("Erreur de récupération des films");
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "Échec de la connexion à l'API : " + t.getMessage(), t);
                showErrorToast("Échec de la connexion à l'API. Vérifiez votre connexion internet.");
            }
        });
    }

    private String getGenreByWeather(String weatherCondition) {
        switch (weatherCondition.toLowerCase()) {
            case "clear":
            case "sunny":
                return "35"; // Comédie
            case "rain":
            case "drizzle":
                return "27"; // Horreur
            case "clouds":
                return "14"; // Fantastique
            case "snow":
                return "10751"; // Famille
            case "storm":
            case "tornado":
                return "878"; // Science-fiction
            case "fog":
                return "53"; // Thriller
            case "overcast":
                return "18"; // Drame
            default:
                return "28"; // Action (par défaut)
        }
    }

    private void showErrorToast(String message) {
        Toast.makeText(SuggestionsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSeeMoreClicked(Movie movie) {
        Log.d(TAG, "Movie clicked: " + movie.getTitle());
        showBottomSheet(movie);
    }


    public void showBottomSheet(Movie movie) {
        if (movie != null) {
            BottomSheetDetailsFragment bottomSheetFragment = BottomSheetDetailsFragment.newInstance(movie);
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        } else {
            Log.e(TAG, "Impossible d'afficher les détails : film nul");
        }
    }

}