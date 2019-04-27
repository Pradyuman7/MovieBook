package com.pd.nextmovie.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.pd.nextmovie.R;
import com.pd.nextmovie.data.HighlightRenderer;
import com.pd.nextmovie.data.HighlightedResult;
import com.pd.nextmovie.model.Movie;

import java.util.Collection;


class MovieAdapter extends ArrayAdapter<HighlightedResult<Movie>> {
    private final Context context;

    private final DisplayImageOptions displayImageOptions;
    private final HighlightRenderer highlightRenderer;
    private ImageLoader imageLoader;

    public MovieAdapter(final Context context, int resource) {
        super(context, resource);
        this.context = context;

        // Configure Universal Image Loader.
        displayImageOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).resetViewBeforeLoading(true).displayer(new FadeInBitmapDisplayer(300)).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                imageLoader = ImageLoader.getInstance();
                if (!imageLoader.isInited()) {
                    ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context).memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13).build();
                    imageLoader.init(configuration);
                }
            }
        }).start();

        highlightRenderer = new HighlightRenderer(context);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View cell = convertView;
        if (cell == null) {
            cell = LayoutInflater.from(context).inflate(R.layout.cell_movie, parent, false);
        }

        ImageView posterImageView = (ImageView) cell.findViewById(R.id.imageview_poster);
        TextView titleTextView = (TextView) cell.findViewById(R.id.textview_title);
        TextView yearTextView = (TextView) cell.findViewById(R.id.textview_year);

        HighlightedResult<Movie> result = getItem(position);

        assert result != null;
        imageLoader.displayImage(result.getResult().getImage(), posterImageView, displayImageOptions);
        titleTextView.setText(highlightRenderer.renderHighlights(result.getHighlight("title").getHighlightedValue()));
        yearTextView.setText(String.format("%d", result.getResult().getYear()));

        return cell;
    }

    @Override
    public void addAll(@NonNull Collection<? extends HighlightedResult<Movie>> items) {
        super.addAll(items);
    }
}