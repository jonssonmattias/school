package se.mau.mattiasjonsson.assignment2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView listView;
    private String[] values = new String[]{"Name: null", "Temperature: null", "Humidity: null", "Pressure: null"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        listView = findViewById(R.id.listView);
        mapFragment.getMapAsync(this);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        setMapLongClick(mMap);
        setOnMarkerClick(mMap);
    }

    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                volleyRequest(latLng);
                map.addMarker(new MarkerOptions().position(latLng).title(latLng.latitude+";"+latLng.longitude));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    private void setOnMarkerClick(final GoogleMap map){
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String[] title = marker.getTitle().split(";");
                double lat = Double.parseDouble(title[0]);
                double lon = Double.parseDouble(title[1]);
                LatLng latLng = new LatLng(lat, lon);
                volleyRequest(latLng);
                return false;
            }
        });
    }

    private void volleyRequest(final LatLng latLng) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String API_KEY = "7f7205229672eebf4f57146158c38bbc";
        String URL = "https://api.openweathermap.org/data/2.5/weather?lat="+latLng.latitude+"&lon="+latLng.longitude+"&appid="+API_KEY;

        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {@Override public void onResponse(String response) {
                    processJSON(response);
                }},
                new Response.ErrorListener() {@Override public void onErrorResponse(VolleyError error) {Log.d("ERROR", error.toString());}}
        );
        queue.add(stringRequest);
    }
    private void processJSON(String str){
        String name="", temp="", humidity="", pressure="";
        JSONObject jsonObject, jsonObject1=null;
        try {
            jsonObject = (JSONObject)new JSONParser().parse(str);
            jsonObject1 = (JSONObject)jsonObject.get("main");
            name = "Name: "+jsonObject.get("name");
            humidity = "Humidity: "+jsonObject1.get("humidity")+" %";
            pressure = "Pressure: "+jsonObject1.get("pressure")+" hPa";
            temp = "Temperature: "+(int)Math.round((float)(double)jsonObject1.get("temp")-273.15)+" C";
        }catch (ParseException e){
            Log.e("ERROR", e.getMessage(), e);
        }
        catch (ClassCastException e){
            temp = "Temperature: "+(int)Math.round((float)(double)jsonObject1.get("temp")-273.15)+" C";
        }
        values = new String[]{name, temp, humidity, pressure};
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values));
    }
}
