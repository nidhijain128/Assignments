package com.example.weatherforecastapp;

import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class imageParser extends AsyncTask<String, Void, Bitmap> {

	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap getImage = null;
		try {

			InputStream in = new java.net.URL(urldisplay).openStream();
			getImage = BitmapFactory.decodeStream(in);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return getImage;
	}
}
