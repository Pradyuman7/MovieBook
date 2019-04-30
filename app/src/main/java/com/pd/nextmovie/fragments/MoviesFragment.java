package com.pd.nextmovie.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.algolia.instantsearch.ui.utils.ItemClickSupport;
import com.algolia.instantsearch.ui.views.Hits;
import com.pd.chocobar.ChocoBar;
import com.pd.nextmovie.R;
import com.pd.nextmovie.activities.MoviesActivity;
import com.pd.nextmovie.asynctask.GetImageFromURI;
import com.pd.nextmovie.model.Bookmarks;
import com.pd.nextmovie.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MoviesFragment extends MoviesActivity.MovieTabActivity.LayoutFragment {

    private Bookmarks bookmarks;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MoviesFragment() {
        super(R.layout.fragment_movies);
        bookmarks = new Bookmarks();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Hits hits = view.findViewById(R.id.hits_movies);

        hits.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, int position, View v) {
                JSONObject jsonObject = hits.get(position);

                Log.d("Clicked_object: ",jsonObject.toString());



                try {
                    Log.d("Clicked","Clicked on the hit "+jsonObject.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        hits.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView recyclerView, int position, View v) {
                JSONObject jsonObject = hits.get(position);

                try {
                    final String movieTitle = jsonObject.getString("title");
                    final String image = jsonObject.getString("image");
                    final int rating = jsonObject.getInt("rating");
                    final int year = jsonObject.getInt("year");

                    Drawable drawable = new GetImageFromURI().execute(image).get();

                    ChocoBar.builder().setBackgroundColor(Color.parseColor("#000000"))
                            .setTextSize(15)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.ITALIC)
                            .setText("Bookmarked "+movieTitle)
                            .setMaxLines(5)
                            .centerText()
                            .setActionText("OK")
                            .setActionClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Movie movie = new Movie(movieTitle, image);
                                    movie.setRating(rating);
                                    movie.setYear(year);

                                    bookmarks.addMovie(movie);
                                    bookmarks.addBookmarksToDatabase();
                                }
                            })
                            .setActionTextColor(Color.parseColor("#66FFFFFF"))
                            .setActionTextSize(20)
                            .setIcon(drawable)
                            .setActivity(MoviesFragment.this.getActivity())
                            .setDuration(ChocoBar.LENGTH_INDEFINITE)
                            .build()
                            .show();


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

    }

}