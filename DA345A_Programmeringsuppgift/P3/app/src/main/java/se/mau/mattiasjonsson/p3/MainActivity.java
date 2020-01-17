package se.mau.mattiasjonsson.p3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import twitter4j.*;
import twitter4j.auth.AccessToken;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private TextView mTextViewLocation, mText_view_temp, mText_view_nederbörd;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final String TAG = "MainActivity";
    private double lon, lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        mButton.setOnClickListener(new PostOnTwitter());
        location();
    }
    private void initComponents() {
        mButton = findViewById(R.id.button_postOnTwitter);
        mTextViewLocation = findViewById(R.id.text_view_location);
        mText_view_temp = findViewById(R.id.text_view_temp);
        mText_view_nederbörd = findViewById(R.id.text_view_nederbörd);
    }
    private void location(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                getWeather(nearestStation(lat,lon));
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, locationListener);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    private void setValue(String values, String type){
        switch (type){
            case "Lufttemperatur": mText_view_temp.setText("Temp: " + values); break;
            case "Nederbördsmängd": mText_view_nederbörd.setText("Nederbörd: " + values); break;
        }
    }
    private void getWeather(String id){
        new WeatherAsynkTask().execute("http://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station/"+id+"/period/latest-hour/data.json");
        new WeatherAsynkTask().execute("https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/7/station/"+id+"/period/latest-hour/data.json");
    }
    public String nearestStation(double myLatitude, double myLongitude) {
        String nearestID="", nearestName = "";
        double minDistance = Double.MAX_VALUE;
        for(int i =0; i<Stations.ID.length;i++) {
            String id = Stations.ID[i];
            double longitude = Double.parseDouble(Stations.LONGITUDE[i]);
            double latitude = Double.parseDouble(Stations.LATITUDE[i]);
            double a = myLatitude-latitude;
            double b = myLongitude-longitude;
            double distance = Math.sqrt((Math.pow(a, 2)) + Math.pow(b, 2));
            if(distance<minDistance && Stations.ACTIVE[i]) {
                minDistance = distance;
                nearestID = id;
                nearestName = Stations.NAME[i];
            }
        }
        mTextViewLocation.setText(nearestName);
        return nearestID;
    }
    private class PostOnTwitter implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new TwitterAsyncTask().execute();
        }
    }
    private class TwitterAsyncTask extends android.os.AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... pVoids) {
            String token = "ACCESS_TOKEN";
            String secret = "SECRET_KEY";
            AccessToken a = new AccessToken(token,secret);
            Twitter twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer("TWITTER_CONSUMER_KEY", "TWITTER_CONSUMER_SECRET");
            twitter.setOAuthAccessToken(a);
            try{
                twitter.updateStatus(message());
                Log.d("POST","posted");
                Toast.makeText(MainActivity.this, "Tweet Posted",
                        Toast.LENGTH_SHORT).show();
            }catch (TwitterException e){
                e.printStackTrace();
                Log.d("POST","not posted");
            }
            return null;
        }
        private String message() {
            return String.format("Location: %s\nTemperature: %s\nPrecipitation: %s", mTextViewLocation.getText().toString(), mText_view_temp.getText().toString(), mText_view_nederbörd.getText().toString());
        }
    }
    private class WeatherAsynkTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }catch(Exception e){
                return "Station not found";
            }
        }
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                String valueObj = new JSONObject(s).getString("value");
                String value = "Not found";
                if(valueObj != "null")
                    value = new JSONArray(valueObj).getJSONObject(0).getString("value");
                String typeObj = new JSONObject(s).getString("parameter");
                String type = new JSONObject(typeObj).getString("name");
                setValue(value, type);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
