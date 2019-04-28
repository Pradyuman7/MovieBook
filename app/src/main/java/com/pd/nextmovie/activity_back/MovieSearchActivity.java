package com.pd.nextmovie.activity_back;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algolia.instantsearch.core.helpers.Searcher;

import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;
import com.pd.nextmovie.R;
import com.pd.nextmovie.data.ResultsListView;


public class MovieSearchActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    // Constants
    private static final int LOAD_MORE_THRESHOLD = 5;
    private static final int HITS_PER_PAGE = 20;

    private Searcher searcher;
    private InstantSearch helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        // Bind UI components.
        // UI:
        ResultsListView moviesListView;
        (moviesListView = findViewById(R.id.listview_movies)).setOnScrollListener(this);

        // Init Algolia.
        Client client = new Client("latency", "dce4286c2833e8cf4b7b1f2d3fa1dbcb");
        searcher = Searcher.create(client.initIndex("movies"));
        helper = new InstantSearch(moviesListView, searcher);

        // Pre-build query.
        searcher.setQuery(new Query().setAttributesToRetrieve("title", "image", "rating", "year").setAttributesToHighlight("title").setHitsPerPage(HITS_PER_PAGE));

        moviesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object object = adapterView.getAdapter().getItem(i);
                Toast.makeText(MovieSearchActivity.this,"You clicked "+object.toString(), Toast.LENGTH_LONG).show();
            }
        });

        moviesListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieSearchActivity.this,"You clicked ", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_search, menu);
        // Configure search view.
        helper.registerSearchView(this, menu, R.id.search);
        return true;
    }

    // AbsListView.OnScrollListener

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Nothing to do.
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Abort if list is empty or the end has already been reached.
        if (totalItemCount == 0 || !searcher.hasMoreHits()) {
            return;
        }

        // Load more if we are sufficiently close to the end of the list.
        int firstInvisibleItem = firstVisibleItem + visibleItemCount;
        if (firstInvisibleItem + LOAD_MORE_THRESHOLD >= totalItemCount) {
            searcher.loadMore();
        }
    }
}