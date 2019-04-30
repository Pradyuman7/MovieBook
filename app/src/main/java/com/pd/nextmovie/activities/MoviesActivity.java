package com.pd.nextmovie.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.instantsearch.ui.viewmodels.SearchBoxViewModel;
import com.algolia.instantsearch.ui.views.SearchBox;
import com.google.firebase.auth.FirebaseAuth;
import com.pd.nextmovie.R;
import com.pd.nextmovie.fragments.CastFragment;
import com.pd.nextmovie.fragments.ForYouFragment;
import com.pd.nextmovie.fragments.MoviesFragment;

import java.util.Objects;


public abstract class MoviesActivity extends AppCompatActivity {
    static final String ALGOLIA_APP_ID = "latency";
    static final String ALGOLIA_INDEX_ACTORS = "actors";
    static final String ALGOLIA_INDEX_MOVIES = "movies";
    static final String ALGOLIA_API_KEY = "d0a23086ed4be550f70be98c0acf7d74";
    static final String EXTRA_QUERY = "query";

    protected Searcher searcherMovies;
    protected Searcher searcherActors;
    protected SearchBoxViewModel searchBoxViewModel;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SearchBox searchBox;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setSupportActionBar(this.<Toolbar>findViewById(R.id.toolbar));
        // Initialize a Searcher with your credentials and an index name
        Searcher.destroyAll();
        searcherMovies = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_MOVIES);
        searcherActors = Searcher.create(ALGOLIA_APP_ID, ALGOLIA_API_KEY, ALGOLIA_INDEX_ACTORS);


    }

    @Override
    protected void onDestroy() {
        searcherMovies.destroy();
        searcherActors.destroy();
        super.onDestroy();
    }

    protected void initWithLayout() {
        this.<FloatingActionButton>findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseAuth.getCurrentUser() != null)
                    startActivity(new Intent(MoviesActivity.this, BookmarkActivity.class));

                else
                    startActivity(new Intent(MoviesActivity.this, RegisterActivity.class));
            }
        });
        if (searchBoxViewModel == null) {
            searchBox = this.findViewById(R.id.include_searchbox);
            searchBoxViewModel = new SearchBoxViewModel(searchBox);
        }
    }


    protected void setQueryFromIntent(Intent intent) {
        if (intent.hasExtra(EXTRA_QUERY)) {
            final String query = intent.getStringExtra(EXTRA_QUERY);
            searchBox.setQuery(query, query != null);
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setQueryFromIntent(intent);
    }

    public static class MovieTabActivity extends MoviesActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_movies);
            initWithLayout();

            // Set up the ViewPager with an adapter that will return a fragment
            // for each of the two primary sections of the activity.
            ViewPager viewPager = findViewById(R.id.container);
            viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
            TabLayout tabLayout = findViewById(R.id.tabs);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        }

        private void linkFragmentToSearcher(LayoutFragment layoutFragment) {
            Searcher searcher = (layoutFragment instanceof MoviesFragment) ? searcherMovies : searcherActors;

            // link the Searcher to the Fragment's UI
            new InstantSearch(this, searcher, layoutFragment).registerSearchView(this, searchBoxViewModel);

            // this is the place where you need to add listeners


            // Show results for empty query (on app launch) / voice query (from intent)
            searcher.search(getIntent());
            // If the activity was started with an intent, apply any query it contains
            setQueryFromIntent(getIntent());

        }

        public static abstract class LayoutFragment extends Fragment {
            private final int layout;

            public LayoutFragment(@LayoutRes int layout) {
                this.layout = layout;
            }

            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(layout, container, false);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                ((MovieTabActivity) Objects.requireNonNull(getActivity())).linkFragmentToSearcher(this);
            }
        }

        public static abstract class LayoutFragmentWithoutAlgolia extends Fragment {
            private final int layout;

            public LayoutFragmentWithoutAlgolia(@LayoutRes int layout) {
                this.layout = layout;
            }

            @Override
            public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(layout, container, false);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
            }
        }

        public class SectionsPagerAdapter extends FragmentPagerAdapter {

            SectionsPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public Fragment getItem(int position) {
                // getItem is called to instantiate the fragment for the given page.
                switch (position) {
                    case 0:
                        return new MoviesFragment();
                    case 1:
                        return new CastFragment();
                    default:
                        return new ForYouFragment();
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        }
    }
}