package com.pd.nextmovie;

public class Highlight {
    private String attributeName;
    private String highlightedValue;

    public Highlight(String attributeName, String highlightedValue) {
        this.attributeName = attributeName;
        this.highlightedValue = highlightedValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getHighlightedValue() {
        return highlightedValue;
    }
}