package com.pd.nextmovie.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
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

import org.json.JSONException;
import org.json.JSONObject;

public class CastFragment extends MoviesActivity.MovieTabActivity.LayoutFragment {
    public CastFragment() {
        super(R.layout.fragment_actors);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Hits hits = view.findViewById(R.id.hits_actors);

        hits.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, int position, View v) {
                JSONObject jsonObject = hits.get(position);

                try {
                    String name = jsonObject.getString("name");

                    ChocoBar.builder().setBackgroundColor(Color.parseColor("#000000"))
                            .setTextSize(20)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.ITALIC)
                            .setText(name)
                            .setMaxLines(5)
                            .centerText()
                            .setActivity(CastFragment.this.getActivity())
                            .setDuration(ChocoBar.LENGTH_SHORT)
                            .build()
                            .show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("cast",jsonObject.toString());
            }
        });
    }
}
