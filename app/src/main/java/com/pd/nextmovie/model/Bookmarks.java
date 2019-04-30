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

    public Movie getMovieAtPosition(int position){
        if(position > bookmarkedMovies.size() || position < 0)
            return null;

        return bookmarkedMovies.get(position);
    }

    public ArrayList<String> getBookmarkedMoviesFromDatabase(){
        final ArrayList<String> bookmarks = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child("bookmarks").getChildren()){
                    bookmarks.add(Objects.requireNonNull(ds.getValue()).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError: ",databaseError.toString());
            }
        });

        return bookmarks;
    }

    public void addBookmarksToDatabase(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        reference.child("bookmarks").setValue(bookmarkedMovies);
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
