package com.example.androidassignments;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends Activity {
    private final String ACTIVITY_NAME = "WeatherForecastActivity";

    ProgressBar progressBar;
    ImageView imageView;
    TextView current_temp;
    TextView min_temp;
    TextView max_temp;
    List <String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        setTitle("Canada Weather Network Information");

        current_temp = findViewById(R.id.textView4);
        min_temp = findViewById(R.id.textView5);
        max_temp = findViewById(R.id.textView6);
        imageView = findViewById(R.id.imageView2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        get_a_city();
    }

    public void get_a_city() {
        cityList = Arrays.asList(getResources().getStringArray(R.array.cities));
        final Spinner citySpinner = findViewById(R.id.spinner);
        ArrayAdapter <CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.cities, android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                new ForecastQuery(cityList.get(i)).execute("this will go to background");
            }
            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });
    }

    private class ForecastQuery extends AsyncTask <String, Integer, String> {
        private String currentTemp;
        private String minTemp;
        private String maxTemp;
        private Bitmap picture;
        protected String city;

        ForecastQuery(String city) {
            this.city = city;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("onPreExecute", " is called");
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.i("incoming param", strings[0] + "---------------------------") ;
            try {
                URL url = new URL(
                        "https://api.openweathermap.org/data/2.5/weather?" +
                                "q=" + this.city + "," + "ca&" +
                                "APPID=79cecf493cb6e52d25bb7b7050ff723c&" +
                                "mode=xml&" +
                                "units=metric");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();

                InputStream in = httpsURLConnection.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);
                    int type;
                    while ((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("temperature")) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                            } else if (parser.getName().equals("weather")) {
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";

                                Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);
                                if (fileExistance(fileName)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(fileName);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME, "Found the file locally");
                                    picture = BitmapFactory.decodeStream(fis);
                                } else {
                                    String iconUrl = "https://openweathermap.org/img/w/" + fileName;
                                    picture = getImage(new URL(iconUrl));

                                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    picture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                    outputStream.flush();
                                    outputStream.close();
                                }
                                publishProgress(100);
                            }
                        }
                        parser.next();
                    }
                } finally {
                    httpsURLConnection.disconnect();
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return " do background ended";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpsURLConnection connection = null;
            try {
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String a) {
            Log.i ("post", a.toString() + "------------------");
            progressBar.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(picture);
            current_temp.setText(currentTemp + "C\u00b0");
            min_temp.setText("Min: "+minTemp + "C\u00b0");
            max_temp.setText("Max: "+maxTemp + "C\u00b0");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i ("vlaues",  values[0] +"-------------------------------") ;
            progressBar.setProgress(values[0]);
        }
    }
}