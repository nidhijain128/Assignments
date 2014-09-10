package com.example.weatherforecastapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class MainActivity extends Activity {
	private static String url = "";
	public static final String APP_ID = "1397528630488302";
	private Facebook facebook;
	private Toast toast;
	@SuppressWarnings("deprecation")
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	TextView cityName, regionCountryName, textName, tempName, forecastName,
			r1c1Name, r1c2Name, r1c3Name, r1c4Name, r2c1Name, r2c2Name,
			r2c3Name, r2c4Name, r3c1Name, r3c2Name, r3c3Name, r3c4Name,
			r4c1Name, r4c2Name, r4c3Name, r4c4Name, r5c1Name, r5c2Name,
			r5c3Name, r5c4Name, postCName, postFName;
	ImageView imgName;
	TableLayout tableDisplayName;
	JSONArray forecast = null;
	JSONObject condition = null;
	JSONObject location = null;
	JSONObject jsonFB = null;
	String link = null;
	String img = null;
	String feed = null;
	JSONObject units = null;
	EditText editText = null;
	String locationS, tempUnit, locationType, text, city, region, temp = "";
	RadioButton rb1, rb2 = null;
	boolean degreeF, degreeC = true;
	int postType;
	ArrayList<String> forecastList = null;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		facebook = new Facebook(APP_ID);
		toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		final Button button = (Button) findViewById(R.id.submitButton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					editText = (EditText) findViewById(R.id.location);
					locationS = editText.getText().toString();
					rb1 = (RadioButton) findViewById(R.id.degreeF);
					degreeF = rb1.isChecked();
					rb2 = (RadioButton) findViewById(R.id.degreeC);
					degreeC = rb2.isChecked();
					int flag = 1;
					if (degreeF)
						tempUnit = "f";
					else if (degreeC)
						tempUnit = "c";
					if (locationS.equals("")) {
					{
						alertbox("Error", "Please enter location or zipcode");
						flag = 0;
					}
					} else if (locationS.matches("[0-9]{5}")) {
						locationType = "zip";
					} else if ((locationS.indexOf(',') != -1)
							&& ((locationS.indexOf(',')) < (locationS.length() - 2))) {
						locationType = "city";
					} else
					{
						flag = 0;
						alertbox("Error", "Invalid location. Invalid location: must inlcude state or country separated by comma\nExample: Los Angeles, CA or a valid 5 digit zipcode");
					}
					
					if(flag == 1)
					{
						locationS = locationS.trim();
						if (locationType.equals("zip")
								|| locationType.equals("city")) {
							url = "http://cs-server.usc.edu:13856/HomeWork8/HomeWork8?location="
									+ locationS
									+ "&locationType="
									+ locationType
									+ "&tempUnit=" + tempUnit;
							url = url.replace(" ","");
							new JSONParse().execute();
						}
					}
				} catch (NullPointerException e) {
				}
			}
		});
	}

	protected void alertbox(String title, String mymessage) {
		new AlertDialog.Builder(this)
				.setMessage(mymessage)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
		clearScreen();
	}
	
	protected void alertbox1(String title, String mymessage) {
		new AlertDialog.Builder(this)
				.setMessage(mymessage)
				.setTitle(title)
				.setCancelable(true)
				.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}
	
	protected void clearScreen() {
		editText.setText("");
		cityName.setText("");
		regionCountryName.setText("");
		imgName.setImageDrawable(null);
		textName.setText("");
		tempName.setText("");
		forecastName.setText("");
		tableDisplayName.setVisibility(View.INVISIBLE);
		postCName.setText("");
		postFName.setText("");
	}
	
	protected void clearScreen1() {
		//editText.setText("");
		//cityName.setText("");
		regionCountryName = null;
		imgName.setImageDrawable(null);
		textName = null;
		tempName = null;
		forecastName = null;
		tableDisplayName.setVisibility(View.INVISIBLE);
		postCName = null;
		postFName = null;
	}

	protected void facebookbox(int a) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		postType = a;
		alert.setTitle("Post to Facebook");
		if (a == 1)
			alert.setNegativeButton("Post Current Weather",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							loginToFacebook();
							// postToWall();
						}
					});
		else if (a == 2)
			alert.setNegativeButton("Post Weather Forecast",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							loginToFacebook();
						}
					});
		alert.setPositiveButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});

		alert.show();
	}

	@SuppressWarnings("deprecation")
	public void postToWall() {
		Bundle bundle = new Bundle();
		try {
			if (postType == 2) {
				String desc = "";
				for (int i = 0; i <= 19; i = i + 4) {
					desc = desc + forecastList.get(i + 2) + ": "
							+ forecastList.get(i) + ", "
							+ forecastList.get(i + 1) + "/"
							+ forecastList.get(i + 3) + "°" + tempUnit.toUpperCase() + "; ";
				}
				bundle.putString("name", city + ", " + region);
				bundle.putString("link", jsonFB.getJSONObject("weather")
						.getString("feed"));
				bundle.putString("picture",
						"http://www-scf.usc.edu/~csci571/2013Fall/hw8/weather.jpg");
				bundle.putString("caption", "Weather Forecast for " + city);
				bundle.putString("description", desc);
				bundle.putString(
						"properties",
						"{\"Look at details\" : {\"text\" : \"here\", \"href\" : \""
								+ jsonFB.getJSONObject("weather").getString(
										"link") + "\"}}");
			} else if (postType == 1) {
				bundle.putString("name", city + ", " + region);
				bundle.putString("link", jsonFB.getJSONObject("weather")
						.getString("feed"));
				bundle.putString("picture", jsonFB.getJSONObject("weather")
						.getString("img"));
				bundle.putString("caption", "The current condition for " + city
						+ " is " + text + " .");
				bundle.putString("description", "Temperature is " + temp + " .");
				bundle.putString(
						"properties",
						"{\"Look at details\" : {\"text\" : \"here\", \"href\" : \""
								+ jsonFB.getJSONObject("weather").getString(
										"link") + "\"}}");
			}
			facebook.dialog(MainActivity.this, "feed", bundle,
					new DialogListener() {
						@Override
						public void onFacebookError(FacebookError e) {
						}

						@Override
						public void onError(DialogError e) {
						}

						@Override
						public void onComplete(Bundle values) {
							String postId = values.getString("post_id");
							if (postId != null) {
								mAsyncRunner.request(postId,
										new RequestListener() {

											@Override
											public void onMalformedURLException(
													MalformedURLException e,
													Object state) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onIOException(
													IOException e, Object state) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onFileNotFoundException(
													FileNotFoundException e,
													Object state) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onFacebookError(
													FacebookError e,
													Object state) {
												// TODO Auto-generated method
												// stub
												//toast.setText("there was an error while connecting to Facebook");
												//toast.show();
											}

											@Override
											public void onComplete(
													String response,
													Object state) {
												// TODO Auto-generated method
												// stub
												
											}
										});
							}
							if(postId!=null)
							{
								alertbox1("Success", "Facebook message posted successfully");
							}
							else
							{
								alertbox1("Failure", "Facebook message not posted");
							}
						}

						@Override
						public void onCancel() {
						}
					});
		} catch (JSONException e) {
		}

	}

	@SuppressWarnings("deprecation")
	public void loginToFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						@Override
						public void onCancel() {
						}

						@Override
						public void onComplete(Bundle values) {
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

						}

						@Override
						public void onError(DialogError error) {
						}

						@Override
						public void onFacebookError(FacebookError fberror) {
						}

					});
		}
		postToWall();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			cityName = (TextView) findViewById(R.id.cityName);
			regionCountryName = (TextView) findViewById(R.id.regionCountryName);
			textName = (TextView) findViewById(R.id.text);
			tempName = (TextView) findViewById(R.id.temp);
			forecastName = (TextView) findViewById(R.id.forecast);
			imgName = (ImageView) findViewById(R.id.imgName);
			r1c1Name = (TextView) findViewById(R.id.r1c1);
			r1c2Name = (TextView) findViewById(R.id.r1c2);
			r1c3Name = (TextView) findViewById(R.id.r1c3);
			r1c4Name = (TextView) findViewById(R.id.r1c4);
			r2c1Name = (TextView) findViewById(R.id.r2c1);
			r2c2Name = (TextView) findViewById(R.id.r2c2);
			r2c3Name = (TextView) findViewById(R.id.r2c3);
			r2c4Name = (TextView) findViewById(R.id.r2c4);
			r3c1Name = (TextView) findViewById(R.id.r3c1);
			r3c2Name = (TextView) findViewById(R.id.r3c2);
			r3c3Name = (TextView) findViewById(R.id.r3c3);
			r3c4Name = (TextView) findViewById(R.id.r3c4);
			r4c1Name = (TextView) findViewById(R.id.r4c1);
			r4c2Name = (TextView) findViewById(R.id.r4c2);
			r4c3Name = (TextView) findViewById(R.id.r4c3);
			r4c4Name = (TextView) findViewById(R.id.r4c4);
			r5c1Name = (TextView) findViewById(R.id.r5c1);
			r5c2Name = (TextView) findViewById(R.id.r5c2);
			r5c3Name = (TextView) findViewById(R.id.r5c3);
			r5c4Name = (TextView) findViewById(R.id.r5c4);
			tableDisplayName = (TableLayout) findViewById(R.id.tableDisplay);
			postCName = (TextView) findViewById(R.id.postCW);
			postFName = (TextView) findViewById(R.id.postFW);
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			jsonFB = json;
			pDialog.dismiss();
			try {
				if (json == null || json.length()==0) {
					clearScreen1();
					cityName.setText("No weather records found");
				} else {
					forecast = new JSONArray(json.getJSONObject("weather")
							.getString("forecast"));
					forecastList = new ArrayList<String>();
					for (int i = 0; i < forecast.length(); i++) {
						JSONObject rec = forecast.getJSONObject(i);
						forecastList.add(rec.getString("text"));
						forecastList.add(rec.getString("high"));
						forecastList.add(rec.getString("day"));
						forecastList.add(rec.getString("low"));
					}
					tableDisplayName.setVisibility(View.VISIBLE);
					forecastName.setText("Forecast");
					postCName.setText("Share Current Weather");
					postFName.setText("Share Weather Forecast");
					forecastName.setVisibility(View.VISIBLE);
					postCName.setVisibility(View.VISIBLE);
					postFName.setVisibility(View.VISIBLE);
					r1c1Name.setText(forecastList.get(2));
					r1c2Name.setText(forecastList.get(0));
					r2c1Name.setText(forecastList.get(6));
					r2c2Name.setText(forecastList.get(4));
					r3c1Name.setText(forecastList.get(10));
					r3c2Name.setText(forecastList.get(8));
					r4c1Name.setText(forecastList.get(14));
					r4c2Name.setText(forecastList.get(12));
					r5c1Name.setText(forecastList.get(18));
					r5c2Name.setText(forecastList.get(16));

					condition = json.getJSONObject("weather").getJSONObject(
							"condition");
					text = condition.getString("text");
					textName.setText(text);
					location = json.getJSONObject("weather").getJSONObject(
							"location");
					city = location.getString("city");
					cityName.setText(city);
					region = location.getString("region") + ", "
							+ location.getString("country");
					regionCountryName.setText(region);
					link = json.getJSONObject("weather").getString("link");
					Bitmap bitmap = new imageParser().execute(
							json.getJSONObject("weather").getString("img"))
							.get();
					imgName.setImageBitmap(bitmap);
					feed = json.getJSONObject("weather").getString("feed");
					units = json.getJSONObject("weather")
							.getJSONObject("units");
					if (degreeF) {
						r1c3Name.setText(forecastList.get(1) + " °F");
						r1c4Name.setText(forecastList.get(3) + " °F");
						r2c3Name.setText(forecastList.get(5) + " °F");
						r2c4Name.setText(forecastList.get(7) + " °F");
						r3c3Name.setText(forecastList.get(9) + " °F");
						r3c4Name.setText(forecastList.get(11) + " °F");
						r4c3Name.setText(forecastList.get(13) + " °F");
						r4c4Name.setText(forecastList.get(15) + " °F");
						r5c3Name.setText(forecastList.get(17) + " °F");
						r5c4Name.setText(forecastList.get(19) + " °F");
						temp = condition.getString("temp") + "\u00b0 " + "F";
					} else if (degreeC) {
						r1c3Name.setText(forecastList.get(1) + " °C");
						r1c4Name.setText(forecastList.get(3) + " °C");
						r2c3Name.setText(forecastList.get(5) + " °C");
						r2c4Name.setText(forecastList.get(7) + " °C");
						r3c3Name.setText(forecastList.get(9) + " °C");
						r3c4Name.setText(forecastList.get(11) + " °C");
						r4c3Name.setText(forecastList.get(13) + " °C");
						r4c4Name.setText(forecastList.get(15) + " °C");
						r5c3Name.setText(forecastList.get(17) + " °C");
						r5c4Name.setText(forecastList.get(19) + " °C");
						temp = condition.getString("temp") + "\u00b0 " + "C";
					}
					tempName.setText(temp);
					postCName.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							facebookbox(1);
						}
					});

					postFName.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							facebookbox(2);
						}
					});
					//json = null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
