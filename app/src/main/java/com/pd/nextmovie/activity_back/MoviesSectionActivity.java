package com.pd.nextmovie.activity_back;


import android.os.Bundle;

import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.pd.nextmovie.R;
import com.pd.nextmovie.activities.MoviesActivity;

public class MoviesSectionActivity extends MoviesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_section);
        initWithLayout();

        new InstantSearch(this, searcherMovies).registerSearchView(this, searchBoxViewModel);
        new InstantSearch(this, searcherActors).registerSearchView(this, searchBoxViewModel);

        // If the activity was started with an intent, apply any query it contains
        setQueryFromIntent(getIntent());
    }
}