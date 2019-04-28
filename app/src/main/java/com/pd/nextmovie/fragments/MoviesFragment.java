package com.pd.nextmovie.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.pd.nextmovie.R;
import com.pd.nextmovie.activities.MoviesActivity;

public class MoviesFragment extends MoviesActivity.MovieTabActivity.LayoutFragment {
    public MoviesFragment() {
        super(R.layout.fragment_movies);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}