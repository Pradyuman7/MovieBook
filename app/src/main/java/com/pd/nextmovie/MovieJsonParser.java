package com.pd.nextmovie;

import org.json.JSONObject;

public class MovieJsonParser {

    public Movie parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        String title = jsonObject.optString("title");
        String image = jsonObject.optString("image");
        int rating = jsonObject.optInt("rating", -1);
        int year = jsonObject.optInt("year", 0);
        if (title != null && image != null && rating >= 0 && year != 0) {
            return new Movie(title, image, rating, year);
        }
        return null;
    }
}