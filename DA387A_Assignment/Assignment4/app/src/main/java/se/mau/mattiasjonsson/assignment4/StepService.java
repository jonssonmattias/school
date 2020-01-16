package se.mau.mattiasjonsson.assignment4;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Objects.isNull;

public class StepService extends Service implements SensorEventListener {

    private LocalBinder binder;
    private DatabaseController databaseController;
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private boolean isStepDetectorPresent = false, firstRecord=true;
    private int steps = 0, stepSessionID;
    private String username;
    private Handler handler;
    private StepFragment stepFragment;
    private Timer stepsTimer = new Timer();
    private long timeStarted;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new LocalBinder();
        handler = new Handler();
        sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        databaseController = new DatabaseController(getApplication());
        isStepDetectorPresent = !isNull(stepDetectorSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR));
        timeStarted = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        stepsTimer.cancel();
        unregisterListener();
        makeToast("Service stopped");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(isStepDetectorPresent) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            makeToast("Registered Step Detector Sensor");
        }
        else
            makeToast("Step Detector not available");
        makeToast("Service bound");
        username = intent.getStringExtra("username");
        databaseController.logStepSession(0, Calendar.getInstance().getTime().toString(), username);
        stepsTimer.scheduleAtFixedRate(new SimulateStep(), 0,1000);
        return binder;
    }

    private void unregisterListener(){
        sensorManager.unregisterListener(this);
        makeToast("Unregister listener");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        makeToast("Service unbound");
        return super.onUnbind(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == stepDetectorSensor){
            int timeSinceStart = (int)(System.currentTimeMillis() - timeStarted)/1000;
            ++steps;
            updateStep(timeSinceStart);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}


    public class LocalBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }

    public void setListener(StepFragment stepFragment) {
        this.stepFragment = stepFragment;
    }

    private void makeToast(final String message){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StepService.this, message, Toast.LENGTH_LONG).show();
            }
        });
        Log.d("StepService", message);
    }

    private void updateStep(int time){
        if(firstRecord){
            stepSessionID = databaseController.getStepSessionID(username);
            Log.d("test1", stepSessionID+"");
            firstRecord=false;
        }
        double stepsPerSec = (double) steps / time;
        stepFragment.updateSteps(steps, stepsPerSec);
        Log.d("test", stepSessionID+"");
        databaseController.updateStepSession(steps, stepSessionID);
    }

    private class SimulateStep extends TimerTask{
        @Override
        public void run() {
            if(!isNull(stepFragment)) {
                int timeSinceStart = (int)(System.currentTimeMillis() - timeStarted)/1000;
                if(new Random().nextBoolean())
                    ++steps;
                updateStep(timeSinceStart);
            }
        }
    }
}