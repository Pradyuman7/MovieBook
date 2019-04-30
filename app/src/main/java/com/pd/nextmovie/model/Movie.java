package com.pd.nextmovie.model;

import java.util.ArrayList;

public class Movie {
    private String title;
    private String image;
    private int rating;
    private int year;
    private ArrayList<String> genre;

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Movie(String title, String image){
        this.title = title;
        this.image = image;
    }

    public Movie(String title, String image, int rating, int year) {
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString(){
        return title+" "+genre.toString()+" "+year+" "+rating;
    }
}
