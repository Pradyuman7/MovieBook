package com.pd.nextmovie.fragments;

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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

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

        final Set<String> genres = new HashSet<>();
        ArrayList<String> allGenres = new ArrayList<>();

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
                        genres.add(genreDS.getValue(String.class));
                    }
                }

                Log.d("genres", genres.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError", databaseError.toString());
            }
        });

        // do training of data

//        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
//        InputStream inputStream = getResources().openRawResource(R.raw.brain);



        if(recommendedMovies.size() == 0){
            ChocoBar.builder().setActivity(ForYouFragment.this.getActivity())
                    .setText("Bookmark some movies of your choice to get recommendations")
                    .setDuration(ChocoBar.LENGTH_LONG)
                    .orange()
                    .show();
        }

    }
}
