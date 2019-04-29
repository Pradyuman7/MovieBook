package com.pd.nextmovie.model;

public class Movie {
    private String title;
    private String image;
    private int rating;
    private int year;

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
}
