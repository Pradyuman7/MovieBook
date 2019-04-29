package com.pd.nextmovie.model;

import java.util.ArrayList;

public class Bookmarks {

    private ArrayList<Movie> bookmarkedMovies;

    public void addMovie(Movie movie){
        bookmarkedMovies.add(movie);
    }

    public Movie getMovieAtPosition(int position){
        if(position > bookmarkedMovies.size() || position < 0)
            return null;

        return bookmarkedMovies.get(position);
    }

    public ArrayList<Movie> getBookmarkedMovies(){
        return bookmarkedMovies;
    }
}
