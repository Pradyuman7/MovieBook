package com.pd.nextmovie.model;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Bookmarks {

    private ArrayList<Movie> bookmarkedMovies;

    public Bookmarks(){
        bookmarkedMovies = new ArrayList<>();
    }

    public void addMovie(Movie movie){
        bookmarkedMovies.add(movie);
    }

    public ArrayList<Movie> getBookmarkedMovies(){
        return bookmarkedMovies;
    }

    public Movie getMovieAtPosition(int position){
        if(position > bookmarkedMovies.size() || position < 0)
            return null;

        return bookmarkedMovies.get(position);
    }

    public void addBookmarksToDatabase(){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("bookmarks").getChildren()) {
                    String title = (String) ds.child("title").getValue();
                    String image = (String) ds.child("image").getValue();
                    long year = (long) ds.child("year").getValue();
                    long rating = (long) ds.child("rating").getValue();

                    ArrayList<String> genres = new ArrayList<>();

                    for (DataSnapshot genreDS : ds.child("genre").getChildren()) {
                        genres.add((String) genreDS.getValue());
                    }

                    Movie movie = new Movie(title, image);
                    movie.setGenre(genres);
                    movie.setRating((int) rating);
                    movie.setYear((int) year);

                    Log.d("movie", movie.toString());

                    if(!checkForTitle(title)){
                        bookmarkedMovies.add(movie);
                    }
                }

                reference.child("bookmarks").setValue(bookmarkedMovies);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError: ",databaseError.toString());
            }
        });

    }

    public boolean checkForTitle(String title){
        for(int i=0;i<bookmarkedMovies.size();i++){
            if(bookmarkedMovies.get(i).getTitle().equals(title)){
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        for(Movie movie : bookmarkedMovies){
            stringBuilder.append(movie.getTitle()).append(",");
        }

        return stringBuilder.toString();
    }
}
