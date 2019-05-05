package com.pd.nextmovie.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.nextmovie.R;
import com.pd.nextmovie.activities.MoviesActivity;

import java.util.ArrayList;

public class ForYouFragment extends MoviesActivity.MovieTabActivity.LayoutFragmentWithoutAlgolia {
    public ForYouFragment() {
        super(R.layout.fragment_for_you);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("uesrs").child(FirebaseAuth.getInstance().getUid()).child("bookmarks");

        final ArrayList<String> genres = new ArrayList<>();
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError", databaseError.toString());
            }
        });

    }
}
