package se.mau.mattiasjonsson.p3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Log.i("Location",location.toString());
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                getWeather(nearestStation(lat,lon));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }



    }

    private void getWeather(String id){
        new WeatherAsynkTask().execute("http://opendata-download-metobs.smhi.se/api/version/1.0/parameter/1/station/"+id+"/period/latest-hour/data.json");
        new WeatherAsynkTask().execute("https://opendata-download-metobs.smhi.se/api/version/1.0/parameter/7/station/"+id+"/period/latest-hour/data.json");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }


    public String nearestStation(double myLatitude, double myLongitude) {
        String nearest = "";
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
                nearest = id;
            }
        }
        return nearest;
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
                Log.d(TAG, "doInBackground: FUCK");
              //  e.printStackTrace();
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

                Log.d("TAG", "onPostExecute:  " + value);
            }catch (Exception e){
                Log.d("Error", s);
            }
        }
    }

}

