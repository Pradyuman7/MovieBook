package com.pd.nextmovie.data;

import com.pd.nextmovie.model.Highlight;
import com.pd.nextmovie.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class SearchResultsJsonParser {
    private MovieJsonParser movieParser = new MovieJsonParser();


    List<HighlightedResult<Movie>> parseResults(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        List<HighlightedResult<Movie>> results = new ArrayList<>();
        JSONArray hits = jsonObject.optJSONArray("hits");
        if (hits == null) return null;

        for (int i = 0; i < hits.length(); ++i) {
            JSONObject hit = hits.optJSONObject(i);
            if (hit == null) continue;

            Movie movie = movieParser.parse(hit);
            if (movie == null)
                continue;

            JSONObject highlightResult = hit.optJSONObject("_highlightResult");
            if (highlightResult == null)
                continue;

            JSONObject highlightTitle = highlightResult.optJSONObject("title");
            if (highlightTitle == null)
                continue;

            String value = highlightTitle.optString("value");
            if (value == null)
                continue;

            HighlightedResult<Movie> result = new HighlightedResult<>(movie);
            result.addHighlight("title", new Highlight("title", value));
            results.add(result);
        }
        return results;
    }
}
