package se.mau.mattiasjonsson.p2.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import se.mau.mattiasjonsson.p2.Controller;
import se.mau.mattiasjonsson.p2.JSONConverter;
import se.mau.mattiasjonsson.p2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener locationListener = new MyLocationListener();
    private Location location;
    private SupportMapFragment mapFragment;
    private HashMap<String, Marker> markers = new HashMap<>();
    private Button btnLogout, btnChat, btnChangeGroup;
    private boolean connected = false;
    private String username, groupName;
    private Controller controller;
    private JSONConverter jsonConverter;
    private ChatFragment chatFragment = new ChatFragment();
    private LoginFragment loginFragment;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initializeComponents(view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("P2", Activity.MODE_PRIVATE);
        username = sharedPreferences.getString("name", null);
        groupName = sharedPreferences.getString("groupName", null);
        mapFragment.getMapAsync(this);
        permission();
        jsonConverter = new JSONConverter();
        if(!connected)
            controller.send(jsonConverter.registerUser(username, groupName));
        new Timer().scheduleAtFixedRate(new LocationTimer(),0, 20000);
        connected = true;
        return view;
    }

    private void initializeComponents(View view){
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChat = view.findViewById(R.id.btnChat);
        btnChangeGroup = view.findViewById(R.id.btnChangeGroup);
        btnLogout.setOnClickListener(new ButtonListener());
        btnChat.setOnClickListener(new ButtonListener());
        btnChangeGroup.setOnClickListener(new ButtonListener());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }

    private void permission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        else
            ((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void updateMyLocation(LatLng location){
        if(markers.containsKey(username))
            markers.get(username).remove();
        markers.put(username, mMap.addMarker(new MarkerOptions().position(location).title(username)));
    }

    public void updateUserLocations(String groupName, HashMap<String, LatLng> locations){
        mMap.clear();
        for(HashMap.Entry<String, LatLng> location : locations.entrySet())
            markers.put(location.getKey(), mMap.addMarker(new MarkerOptions().position(location.getValue()).title(location.getKey())));
    }

    public void setController(Controller controller) {
        this.controller = controller;
        controller.setFragment(this);
    }

    public void setLoginFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void logout(){
        controller.send(jsonConverter.unregisterUser(controller.getUserID()));
        controller.disconnect();
        connected=false;
        setFragment(loginFragment, true);
    }

    private void openChat(){
        chatFragment.setController(controller);
        setFragment(chatFragment, true);
    }

    public void openChangeGroup(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ChangeGroupFragment changeGroupFragment = new ChangeGroupFragment();
        changeGroupFragment.setController(controller);
        changeGroupFragment.setCurrentGroup(groupName);
        changeGroupFragment.setUsername(username);
        changeGroupFragment.setMapFragment(this);
        changeGroupFragment.show(ft, "changeGroupFragment");
    }

    public void setFragment (Fragment fragment, boolean backstack) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, fragment);
        if(backstack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setMyLocation(Location location){
        this.location = location;
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLogout:logout();break;
                case R.id.btnChat:openChat();break;
                case R.id.btnChangeGroup:openChangeGroup(); break;
            }
        }
    }

    private class LocationTimer extends TimerTask {
        @Override
        public void run() {
            try{
                String userID = controller.getUserID();
                controller.send(jsonConverter.sendMyLocation(new LatLng(location.getLatitude(), location.getLongitude()), userID));
            }catch (Exception ex){
                Log.d("EXCEPTION", ex.getMessage());
            }

        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(final Location location) {
            if(connected) {
                setMyLocation(location);
                updateMyLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                chatFragment.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        }
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
}
