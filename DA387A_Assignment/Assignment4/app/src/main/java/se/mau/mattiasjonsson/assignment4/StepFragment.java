package se.mau.mattiasjonsson.assignment4;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.mau.mattiasjonsson.assignment4.database.StepSession;

import static java.util.Objects.isNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    private float currentDegree, lastDegree;
    private ImageView ivCompass;
    private Button btnStart, btnStop, btnHistory;
    private TextView tvSteps, tvStepsPerSecond;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor;
    private SensorEventListener sensorEventListener = new SensorListener();
    private boolean isAccelerometerPresent, isMagnetometerPresent, isLastAccelerometerSet, isLastMagnetometerSet, isSessionStarted=false, isBound;
    private float [] lastAccelerometer,lastMagnetometer, orientation, rotationMatrix;
    private long lastTime = 0;
    private Intent stepsIntent;
    private String username;
    private DatabaseController databaseController;
    private StepService service;
    private StepServiceConnection stepServiceConnection;


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        databaseController = new DatabaseController(getActivity().getApplication());
        stepServiceConnection = new StepServiceConnection(this);
        initComps(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkSensor();
        toggleButtonEnable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListener();
    }

    public void setUsername(String username){
        this.username=username;
    }

    private void unregisterListener(){
        sensorManager.unregisterListener(sensorEventListener);
        Toast.makeText(getContext(),"Unregister listener", Toast.LENGTH_SHORT).show();
    }

    private void checkSensor(){
        isAccelerometerPresent = !isNull(accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        isMagnetometerPresent = !isNull(magnetometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
    }

    private void startListener(){
        if(isAccelerometerPresent) {
            sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getContext(), "Registered Accelerometer Sensor", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), "Accelerometer Sensor not available", Toast.LENGTH_SHORT).show();
        if(isMagnetometerPresent){
            sensorManager.registerListener(sensorEventListener, magnetometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getContext(), "Registered Magnetometer Sensor", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), "Magnetometer Sensor not available", Toast.LENGTH_SHORT).show();
    }

    private void toggleButtonEnable(){
        btnStart.setEnabled(!isSessionStarted);
        btnStop.setEnabled(isSessionStarted);
        btnHistory.setEnabled(!isSessionStarted);
    }

    private void initComps(View view){
        ivCompass = view.findViewById(R.id.ivCompass);
        btnStart = view.findViewById(R.id.btnStart);
        btnHistory = view.findViewById(R.id.btnHistory);
        btnStop = view.findViewById(R.id.btnStop);
        tvSteps = view.findViewById(R.id.tvSteps);
        tvStepsPerSecond = view.findViewById(R.id.tvStepsPerSec);
        btnStart.setOnClickListener(new ButtonListener());
        btnStop.setOnClickListener(new ButtonListener());
        btnHistory.setOnClickListener(new ButtonListener());
    }

    private void animateImageView(float angleInDegrees) {
        RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, - angleInDegrees, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(250);
        rotateAnimation.setFillAfter(true);
        ivCompass.startAnimation(rotateAnimation);
        lastDegree = currentDegree;
        currentDegree =  - angleInDegrees;
    }

    private void resetCompass(){
        animateImageView( currentDegree + 360f);
        animateImageView(currentDegree - 360f);
    }

    public void updateSteps(final int steps, final double stepsPerSec){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSteps.setText("Steps: "+steps);
                tvStepsPerSecond.setText("Steps per second: "+stepsPerSec);
            }
        });
    }

    private void startService(){
        stepsIntent = new Intent(getActivity(),StepService.class);
        stepsIntent.putExtra("username", username);
        getActivity().bindService(stepsIntent, stepServiceConnection, Context.BIND_AUTO_CREATE);
        isBound=true;
    }

    private void stopService(){
        stepsIntent = new Intent(getActivity(), StepService.class);
        if (isBound) {
            getActivity().unbindService(stepServiceConnection);
            isBound = false;
        }
    }

    private void getHistory(){
        ArrayList<String> historyList = new ArrayList<>();
        for(StepSession stepSession : databaseController.getStepSessions(username))
            historyList.add(stepSession.getTime()+" Steps: "+stepSession.getSteps());
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setStepHistory(historyList);
        historyFragment.setUsername(username);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, historyFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            long currentTime = System.currentTimeMillis();
            if (event.sensor == accelerometerSensor) {
                lastAccelerometer = event.values;
                isLastAccelerometerSet = true;
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                double total = Math.sqrt(x*x + y*y + z*z);
                if (total > 20)
                    resetCompass();
            }
            if (event.sensor == magnetometerSensor){
                lastMagnetometer = event.values;
                isLastMagnetometerSet = true;
            }

            if (isLastAccelerometerSet && isLastMagnetometerSet && currentTime - lastTime >= 250) {
                rotationMatrix = new float[9];
                SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
                orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                float azimuthInDegrees = (float) ((Math.toDegrees(orientation[0]) + 360) % 360);
                animateImageView(azimuthInDegrees);
                lastTime = currentTime;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnStart:
                    Log.d("test", "Start");
                    isSessionStarted=true;
                    toggleButtonEnable();
                    startListener();
                    startService();
                    break;
                case R.id.btnStop:
                    Log.d("test", "Stop");
                    isSessionStarted=false;
                    toggleButtonEnable();
                    stopService();
                    unregisterListener();
                    break;
                case R.id.btnHistory:
                    Log.d("test", "History");
                    getHistory();
                    break;

            }
        }
    }

    private class StepServiceConnection implements ServiceConnection {

        private StepFragment stepFragment;

        public StepServiceConnection(StepFragment stepFragment) {
            this.stepFragment = stepFragment;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            StepService.LocalBinder binder = (StepService.LocalBinder) service;
            stepFragment.service = binder.getService();
            stepFragment.isBound = true;
            stepFragment.service.setListener(stepFragment);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            stepFragment.isBound = false;
        }
    }
}
