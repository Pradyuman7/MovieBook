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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MoviesFragment extends MoviesActivity.MovieTabActivity.LayoutFragment {
    public MoviesFragment() {
        super(R.layout.fragment_movies);
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

//                ChocoBar.builder().setBackgroundColor(Color.parseColor("#00bfff"))
//                        .setTextSize(18)
//                        .setTextColor(Color.parseColor("#FFFFFF"))
//                        .setTextTypefaceStyle(Typeface.ITALIC)
//                        .setText("This is a custom Chocobar")
//                        .setMaxLines(4)
//                        .centerText()
//                        .setActionText("ChocoBar")
//                        .setActionTextColor(Color.parseColor("#66FFFFFF"))
//                        .setActionTextSize(20)
//                        .setIcon(R.drawable.common_full_open_on_phone)
//                        .setActivity(MoviesFragment.this.getActivity())
//                        .setDuration(ChocoBar.LENGTH_INDEFINITE)
//                        .build()
//                        .show();

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
                    String movieTitle = jsonObject.getString("title");
                    String image = jsonObject.getString("image");

                    Drawable drawable = new GetImageFromURI().execute(image).get();

                    ChocoBar.builder().setBackgroundColor(Color.parseColor("#000000"))
                            .setTextSize(15)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.ITALIC)
                            .setText("Do you want to bookmark "+movieTitle)
                            .setMaxLines(5)
                            .centerText()
                            .setActionText("OK")
                            .setActionClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

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