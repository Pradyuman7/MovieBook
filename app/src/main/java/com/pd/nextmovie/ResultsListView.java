package com.pd.nextmovie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ListView;

import com.algolia.instantsearch.core.model.AlgoliaResultsListener;
import com.algolia.instantsearch.core.model.SearchResults;

import java.util.List;


public class ResultsListView extends ListView implements AlgoliaResultsListener {
    private final MovieAdapter adapter;
    private SearchResultsJsonParser resultsParser = new SearchResultsJsonParser();

    public ResultsListView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setAdapter(adapter = new MovieAdapter(context, R.layout.cell_movie));
    }

    @Override public void onResults(@NonNull SearchResults results, boolean isLoadingMore) {
        if (!isLoadingMore) {
            List<HighlightedResult<com.pd.nextmovie.Movie>> resultList = resultsParser.parseResults(results.content);
            adapter.clear();
            adapter.addAll(resultList);
            // Scroll the list back to the top.
            smoothScrollToPosition(0);
        }
        else {
            List<HighlightedResult<com.pd.nextmovie.Movie>> resultList = resultsParser.parseResults(results.content);
            adapter.addAll(resultList);
        }
    }
}
