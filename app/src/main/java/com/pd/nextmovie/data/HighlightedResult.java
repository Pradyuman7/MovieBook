package com.pd.nextmovie.data;

import com.pd.nextmovie.model.Highlight;

import java.util.HashMap;
import java.util.Map;

public class HighlightedResult<T> {
    private T result;
    private Map<String, Highlight> highlights = new HashMap<>();

    public HighlightedResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public Highlight getHighlight(String attributeName) {
        return highlights.get(attributeName);
    }

    public void addHighlight(String attributeName, Highlight highlight) {
        highlights.put(attributeName, highlight);
    }
}
