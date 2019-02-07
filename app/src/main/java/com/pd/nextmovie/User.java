package com.pd.nextmovie;

import java.util.ArrayList;

public class User {

    private String name;
    private String favoriteMovie;
    private ArrayList<String> topMovies;

    public User(String name, String favoriteMovie){
        this.name = name;
        this.favoriteMovie = favoriteMovie;
    }

    public String getName(){
        return name;
    }

    public ArrayList<String> getTopMovies() {
        return topMovies;
    }

    public void addTopMovie(String topMovie){
        topMovies.add(topMovie);
    }

    public void setTopMovies(ArrayList<String> topMovies){
        this.topMovies = topMovies;
    }

    public String getFavoriteMovie(){
        return favoriteMovie;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setFavoriteMovie(String favoriteMovie){
        this.favoriteMovie = favoriteMovie;
    }
}
