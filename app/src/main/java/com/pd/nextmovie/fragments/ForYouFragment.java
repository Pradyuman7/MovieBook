package com.pd.nextmovie.fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.chocobar.ChocoBar;
import com.pd.nextmovie.R;
import com.pd.nextmovie.activities.MoviesActivity;
import com.pd.nextmovie.model.Movie;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.liquidplayer.javascript.JSContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ForYouFragment extends MoviesActivity.MovieTabActivity.LayoutFragmentWithoutAlgolia {
    public ForYouFragment() {
        super(R.layout.fragment_for_you);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView recommendedListview = view.findViewById(R.id.recommended);
        ArrayList<Movie> recommendedMovies = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid()).child("bookmarks");

        final Set<String> interestingGenres = new HashSet<>();
        final Set<String> allGenres = new HashSet<>();

        allGenres.add("Action");
        allGenres.add("Adventure");
        allGenres.add("Comedy");
        allGenres.add("Crime");
        allGenres.add("Drama");
        allGenres.add("Fantasy");
        allGenres.add("Historical");
        allGenres.add("Historical Fiction");
        allGenres.add("Horror");
        allGenres.add("Magical realism");
        allGenres.add("Mystery");
        allGenres.add("Paranoid Fiction");
        allGenres.add("Philosophical");
        allGenres.add("Political");
        allGenres.add("Romance");
        allGenres.add("Saga");
        allGenres.add("Satire");
        allGenres.add("Science Fiction");
        allGenres.add("Social");
        allGenres.add("Speculative");
        allGenres.add("Thriller");
        allGenres.add("Urban");
        allGenres.add("War");
        allGenres.add("Western");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot genreDS : ds.child("genre").getChildren()){
                        String newGenre = genreDS.getValue(String.class);

                        interestingGenres.add(newGenre);
                        allGenres.add(newGenre);
                    }
                }

                Log.d("genres", interestingGenres.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError", databaseError.toString());
            }
        });

        // do data working using brain.js and recommend-movie.js







        if(recommendedMovies.size() == 0){
            ChocoBar.builder().setActivity(ForYouFragment.this.getActivity())
                    .setText("Bookmark some movies of your choice to get recommendations")
                    .centerText()
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .setActionText("OK")
                    .setActionTextColor(Color.parseColor("#66FFFFFF"))
                    .orange()
                    .show();
        }

    }
}
