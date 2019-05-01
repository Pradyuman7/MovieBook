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
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.pd.chocobar.ChocoBar;
import com.pd.nextmovie.R;
import com.pd.nextmovie.activities.MoviesActivity;
import com.pd.nextmovie.asynctask.GetImageFromURI;
import com.pd.nextmovie.model.Bookmarks;
import com.pd.nextmovie.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                String title;
                String image;
                String genre;
                int year;
                int rating;
                double score;

                try {
                    title = jsonObject.getString("title");
                    image = jsonObject.getString("image");

                    JSONArray jsonArray = jsonObject.getJSONArray("genre");
                    StringBuilder sb = new StringBuilder();
                    for(int i=0;i<jsonArray.length();i++){
                        sb.append(jsonArray.get(i)).append(", ");
                    }

                    genre = sb.toString();

                    year = jsonObject.getInt("year");
                    rating = jsonObject.getInt("rating");
                    score = jsonObject.getDouble("score");

                    Drawable drawable = new GetImageFromURI().execute(image).get();

                    MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(MoviesFragment.this.getContext())
                            .setTitle(title)
                            .setIcon(drawable)
                            .setDescription("Released in "+year+", having MovieBook score of "+score+" and average rating of "+rating+" falling categories like "+genre)
                            .build();

                    dialog.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //Log.d("Clicked_object: ",jsonObject.toString());

                // use a custom action box to show more details about the movie



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

                    if(FirebaseAuth.getInstance().getUid() == null){
                        ChocoBar.builder().setActivity(MoviesFragment.this.getActivity())
                                .setText("Please login first by clicking on the bookmark button")
                                .setDuration(ChocoBar.LENGTH_SHORT)
                                .orange()
                                .show();
                    }
                    else {
                        final String movieTitle = jsonObject.getString("title");
                        final String image = jsonObject.getString("image");
                        final int rating = jsonObject.getInt("rating");
                        final int year = jsonObject.getInt("year");
                        final JSONArray genre = jsonObject.getJSONArray("genre");

                        //Log.d("genre: ",genre.toString());

                        Drawable drawable = new GetImageFromURI().execute(image).get();

                        ChocoBar.builder().setBackgroundColor(Color.parseColor("#000000"))
                                .setTextSize(15)
                                .setTextColor(Color.parseColor("#FFFFFF"))
                                .setTextTypefaceStyle(Typeface.ITALIC)
                                .setText("Bookmarked " + movieTitle)
                                .setMaxLines(5)
                                .centerText()
                                .setActionText("OK")
                                .setActionClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Movie movie = new Movie(movieTitle, image);
                                        ArrayList<String> movieGenre = new ArrayList<>();

                                        for(int i=0;i<genre.length();i++){
                                            try {
                                                movieGenre.add(genre.getString(i));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        movie.setRating(rating);
                                        movie.setYear(year);
                                        movie.setGenre(movieGenre);

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
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

    }

}