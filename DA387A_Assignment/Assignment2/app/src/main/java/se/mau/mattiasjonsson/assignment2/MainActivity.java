package se.mau.mattiasjonsson.assignment2;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.hardware.*;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.isNull;

public class MainActivity extends AppCompatActivity {
    private LocationManager mLocationManager;
    private LocationListener deviceLocationListener = new DeviceLocationListener();
    private SensorManager mSensorManager;
    private SensorEventListener sensorEventListener = new SensorListener();
    private boolean isTempSensorPresent, isHumiditySensorPresent, isPressureSensorPresent;
    private float atmosphericPressure;
    private Sensor mTempSensor, mHumiditySensor, mPressureSensor;
    private TextView tvMobileTemp, tvMobileHumidity, tvMobilePressure, tvMobileAltitude, tvTimestamp, tvAPITemp, tvAPIHumidity, tvAPIPressure, tvDiffTemp, tvDiffHumidity, tvDiffPressure;
    private Button btnMap;
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private int mobileTemp, mobileHumidity, mobilePressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void initComps(){
        tvMobileTemp = findViewById(R.id.tvMobileTemp);
        tvMobileHumidity = findViewById(R.id.tvMobileHumidity);
        tvMobilePressure = findViewById(R.id.tvMobilePressure);
        tvMobileAltitude = findViewById(R.id.tvMobileAltitude);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvAPITemp = findViewById(R.id.tvAPITemp);
        tvAPIHumidity = findViewById(R.id.tvAPIHumidity);
        tvAPIPressure = findViewById(R.id.tvAPIPressure);
        tvDiffTemp = findViewById(R.id.tvDiffTemp);
        tvDiffHumidity = findViewById(R.id.tvDiffHumidity);
        tvDiffPressure = findViewById(R.id.tvDiffPressure);
        btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new ButtonListener());
    }

    private void checkSensor(){
        isTempSensorPresent = !isNull(mTempSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));
        isHumiditySensorPresent = !isNull(mHumiditySensor=mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY));
        isPressureSensorPresent = !isNull(mPressureSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
    }

    private void locationPermission(){
        mLocationManager = ((LocationManager) getSystemService(Context.LOCATION_SERVICE));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        else
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, deviceLocationListener);
    }

    private void startListener(){
        if(isTempSensorPresent) {
            mSensorManager.registerListener(sensorEventListener, mTempSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this,"Register listener on Ambient Temperature Sensor", Toast.LENGTH_SHORT);
        }
        else tvMobileTemp.setText("Ambient Temperature Sensor is not available");
        if(isHumiditySensorPresent) {
            mSensorManager.registerListener(sensorEventListener, mHumiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this,"Register listener on Relative Humidity Sensor", Toast.LENGTH_SHORT);
        }
        else tvMobileHumidity.setText("Relative Humidity Sensor is not available");
        if(isPressureSensorPresent){
            mSensorManager.registerListener(sensorEventListener, mPressureSensor,SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this,"Register listener on Pressure Sensor", Toast.LENGTH_SHORT);
        }
        else tvMobilePressure.setText("Pressure Sensor is not available");
    }

    private void processJSON(String str){
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(str);
            JSONObject jsonObject1 = (JSONObject)jsonObject.get("main");
            String temp = "Temperature: "+(int)Math.round((float)(double)jsonObject1.get("temp")-273.15)+" C";
            String humidity = "Humidity: "+jsonObject1.get("humidity")+" %";
            String pressure = "Pressure: "+jsonObject1.get("pressure")+" hPa";
            atmosphericPressure = Float.parseFloat(jsonObject1.get("pressure").toString());
            tvAPITemp.setText(temp);
            tvAPIPressure.setText(pressure);
            tvAPIHumidity.setText(humidity);
            tvDiffTemp.setText("Difference temperature: "+(int)Math.round((float)(double)jsonObject1.get("temp")-273.15-mobileTemp)+" C");
            tvDiffHumidity.setText("Difference humidity: "+((long)jsonObject1.get("humidity")-mobileHumidity)+" %");
            tvDiffPressure.setText("Difference pressure: "+((long)jsonObject1.get("pressure")-mobilePressure)+" hPa");
            final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
            tvTimestamp.setText("Timestamp: "+timeStamp);
        }catch (ParseException e){
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void volleyRequest(double lat, double lon) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String API_KEY = "7f7205229672eebf4f57146158c38bbc";
        String URL = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid="+API_KEY;
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {@Override public void onResponse(String response) { processJSON(response); }},
                new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) {Log.d("ERROR", error.toString());}}
        );
        queue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initComps();
        locationPermission();
        mSensorManager =(SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        checkSensor();
        startListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Test","Destroy" );
        unregisterListener();
        mLocationManager.removeUpdates(deviceLocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Test","Pause" );
        unregisterListener();
        mLocationManager.removeUpdates(deviceLocationListener);
    }

    private void unregisterListener(){
        mSensorManager.unregisterListener(sensorEventListener);
        Toast.makeText(this,"Unregister listener", Toast.LENGTH_SHORT);
    }

    private class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()){
                case Sensor.TYPE_AMBIENT_TEMPERATURE:
                    mobileTemp = (int)event.values[0];
                    tvMobileTemp.setText("Temp: "+mobileTemp+" C");break;
                case Sensor.TYPE_RELATIVE_HUMIDITY:
                    mobileHumidity = (int)event.values[0];
                    tvMobileHumidity.setText("Humidity: "+mobileHumidity+"%");break;
                case Sensor.TYPE_PRESSURE:
                    mobilePressure = (int)event.values[0];
                    tvMobilePressure.setText("Pressure: "+mobilePressure+" hPa");
                    tvMobileAltitude.setText("Altitude: "+Math.round(mSensorManager.getAltitude(atmosphericPressure, mobilePressure))+" m");
                    break;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }

    private class GetFromAPI implements Runnable{
        Location location;
        public GetFromAPI(Location location){
            this.location=location;
        }
        @Override
        public void run() {
            volleyRequest(location.getLatitude(), location.getLongitude());
        }
    }

    private class DeviceLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(final Location location) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        executor.execute(new GetFromAPI(location));
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    }
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
