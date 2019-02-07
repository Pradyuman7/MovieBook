package com.pd.nextmovie;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.viewmodels.SearchBoxViewModel;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.google.firebase.auth.FirebaseAuth;


public abstract class MoviesActivity extends AppCompatActivity {
    static final String ALGOLIA_APP_ID = "latency";
    static final String ALGOLIA_INDEX_ACTORS = "actors";
    static final String ALGOLIA_INDEX_MOVIES = "movies";
    static final String ALGOLIA_API_KEY = "d0a23086ed4be550f70be98c0acf7d74";
    static final String EXTRA_QUERY = "query";

    protected Searcher searcherMovies;
    protected Searcher searcherActors;
    protected SearchBoxViewModel searchBoxViewModel;
    private FirebaseAuth.AuthStateListener mAL;
    private SearchBox searchBox;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setSupportActionBar(this.<Toolbar>findViewById(R.id.toolbar));
        // Initialize a Searcher with your credentials and an index name
        Searcher.destroyAll();
        searcherMovies = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_MOVIES);
        searcherActors = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_ACTORS);
    }

    @Override
    protected void onDestroy() {
        searcherMovies.destroy();
        searcherActors.destroy();
        super.onDestroy();
    }

    protected void initWithLayout() {
        this.<FloatingActionButton>findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAL = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser() != null){
                            startActivity(new Intent(MoviesActivity.this, Recommend.class));
                        }
                        else
                            startActivity(new Intent(MoviesActivity.this, FullscreenActivity.class));
                    }
                };


            }
        });
        if (searchBoxViewModel == null) {
            searchBox = this.findViewById(R.id.include_searchbox);
            searchBoxViewModel = new SearchBoxViewModel(searchBox);
        }
    }


    protected void setQueryFromIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_QUERY)) {
            final String query = intent.getStringExtra(EXTRA_QUERY);
            searchBox.setQuery(query, query != null);
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setQueryFromIntent(intent);
    }
}