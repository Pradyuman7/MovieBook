package com.pd.nextmovie.asynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImageFromURI extends AsyncTask<String, Void, Drawable> {
    @Override
    protected Drawable doInBackground(String... strings) {
        String url = strings[0];

        try {

            HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
            connection.setRequestProperty("User-agent","Mozilla/4.0");
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 500, true);

            return new BitmapDrawable(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
