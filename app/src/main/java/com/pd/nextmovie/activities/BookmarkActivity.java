package com.pd.nextmovie.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pd.chocobar.ChocoBar;
import com.pd.nextmovie.R;
import com.pd.nextmovie.asynctask.GetImageFromURI;
import com.pd.nextmovie.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class BookmarkActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        final ListView bookmarkList = findViewById(R.id.bookmarkList);
        final ArrayList<Movie> bookmarks = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));

        final ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child("bookmarks").getChildren()){
                    String title = (String) ds.child("title").getValue();
                    String image = (String) ds.child("image").getValue();
                    long year = (long) ds.child("year").getValue();
                    long rating = (long) ds.child("rating").getValue();

                    ArrayList<String> genres = new ArrayList<>();

                    for(DataSnapshot genreDS : ds.child("genre").getChildren()){
                        genres.add((String) genreDS.getValue());
                    }

                    Movie movie = new Movie(title,image);
                    movie.setGenre(genres);
                    movie.setRating((int) rating);
                    movie.setYear((int) year);

                    Log.d("movie",movie.toString());

                    bookmarks.add(movie);

                    ArrayList<String> movieTitles = new ArrayList<>();
                    ArrayList<String> moviePosterUrl = new ArrayList<>();

                    Log.d("movieList ",bookmarks.toString());

                    for (Movie movie1 : bookmarks) {
                        movieTitles.add(movie1.getTitle());
                        moviePosterUrl.add(movie1.getImage());

                        Log.d("added_movie", "success");
                    }

                    ArrayList<HashMap<String, String>> bookmarkMap = new ArrayList<>();

                    for (int i = 0; i < bookmarks.size(); i++) {
                        HashMap<String, String> hm = new HashMap<>();

                        hm.put("listview_item_title", movieTitles.get(i));
                        hm.put("listview_image", moviePosterUrl.get(i));

                        bookmarkMap.add(hm);

                        Log.d("movie_hashmap_array", "success");
                    }

                    String[] from = {"listview_image", "listview_item_title", "listview_item_short_description"};
                    int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

                    SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRep) {
                            if (view.getId() == R.id.listview_item_title) {
                                ((TextView) view).setText((String) data);
                                return true;
                            } else if (view.getId() == R.id.listview_image) {
                                if (!data.equals("none")) {
                                    Glide.with(BookmarkActivity.this).load(data).into((ImageView) view);
                                }
                                return true;
                            } else if (view.getId() == R.id.listview_item_short_description) {
                                ((TextView) view).setText((String) data);
                                return true;
                            }

                            return false;
                        }
                    };

                    SimpleAdapter simpleAdapter = new SimpleAdapter(BookmarkActivity.this, bookmarkMap, R.layout.activity_list_view, from, to);
                    simpleAdapter.setViewBinder(viewBinder);

                    bookmarkList.setAdapter(simpleAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError: ",databaseError.toString());
            }
        });

        bookmarkList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String, String> item = (HashMap<String, String>) bookmarkList.getItemAtPosition(i);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
                ref.child("favoriteMovie").setValue(item);

                //Log.d("hashmap", item.toString());

                try {
                    Drawable drawable = new GetImageFromURI().execute(item.get("listview_image")).get();

                    ChocoBar.builder().setBackgroundColor(Color.parseColor("#000000"))
                            .setTextSize(18)
                            .setTextColor(Color.parseColor("#FFFFFF"))
                            .setTextTypefaceStyle(Typeface.ITALIC)
                            .setText("Marked "+item.get("listview_item_title")+" as your favourite movie")
                            .setMaxLines(4)
                            .centerText()
                            .setActionText("Ok")
                            .setActionTextColor(Color.parseColor("#66FFFFFF"))
                            .setActionTextSize(20)
                            .setActionTextTypefaceStyle(Typeface.BOLD)
                            .setIcon(drawable)
                            .setActivity(BookmarkActivity.this)
                            .setDuration(ChocoBar.LENGTH_INDEFINITE)
                            .build()
                            .show();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });

        // recommendation system without using * actual * machine learning, trying to figure out how to do so
        // algorithm:
        // 1. go through the bookmarks and find genres that are common and store the common genre list
        // 2. find movies with genre list much similar to the common list of the user to recommend movies
        // 3. remove those movies that are already in the bookmark list

        final Set<String> commonGenres = new HashSet<>(); // currently using set, but employ hashMap to find genres that are more common
        assert FirebaseAuth.getInstance().getUid() != null;
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.child("bookmarks").getChildren()){
                    for(DataSnapshot genreDS : ds.child("genres").getChildren()){
                        String genre = (String) genreDS.getValue();

                        commonGenres.add(genre);
                    }
                }

                List<String> common = new ArrayList<>(commonGenres);
                ref.child("liking").setValue(common);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError", databaseError.toString());
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recommend_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BookmarkActivity.this, MoviesActivity.MovieTabActivity.class));
                finish();
                finish();
                return true;
            case R.id.favourite:
                // give place to set favourite movie
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
