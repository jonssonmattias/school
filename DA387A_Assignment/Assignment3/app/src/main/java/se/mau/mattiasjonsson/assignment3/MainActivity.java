package se.mau.mattiasjonsson.assignment3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static java.util.Objects.isNull;

public class MainActivity extends AppCompatActivity {

    private static final boolean LIGHT_HIGH_STATE = true, LIGHT_LOW_STATE = false;
    private static final int WRITE_SETTINGS = 1, PROXIMITY_SENSOR_THRESHOLD = 1, MORSE_UNIT=500;
    private SensorManager mSensorManager;
    private Sensor mLightSensor, mProximitySensor;
    private SensorEventListener mSensorEventListener = new SensorListener();
    private boolean isLightSensorPresent, isProximitySensorPresent, systemBrightness, lastState, currentState, reading, sending=false;
    private float lightLevel, brightnessLevel;
    private long signalMillisStart = 0, signalMillisEnd = 0;
    private StringBuilder morseCode = new StringBuilder();
    private CameraCharacteristics cameraCharacteristics;
    private CameraManager cameraManager;
    private String cameraID;
    private String[] presets = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5"};
    private double[] presetValues = new double[2];
    private Switch switchSystemBrightness;
    private TextView tvFlashlight, tvMorseCode;
    private Button btnSendSOS, btnSendOK, btnStartReading, btnStopReading;
    private ContentResolver contentResolver;
    private Window window;
    private SeekBar sbBrightness;
    private Spinner spPresets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String test = MorseCode.parse("...---...");
        String test1 = MorseCode.toMorse("SOS");
        Log.d("testMorse", test+"\n"+test1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager =(SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        initComps();
        initCamera();
        initScreenBrightness();
        checkSensor();
        startListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Test","Destroy" );
        unregisterListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Test","Pause" );
        unregisterListener();
    }

    private void initComps(){
        switchSystemBrightness = findViewById(R.id.switchSystemBrightness);
        systemBrightness=switchSystemBrightness.isChecked();
        tvFlashlight = findViewById(R.id.tvFlashlight);
        tvMorseCode = findViewById(R.id.tvMorseCode);
        btnSendSOS=findViewById(R.id.btnSendSOS);
        btnSendOK=findViewById(R.id.btnSendOK);
        btnStartReading=findViewById(R.id.button);
        btnStopReading=findViewById(R.id.button2);
        switchSystemBrightness=findViewById(R.id.switchSystemBrightness);
        sbBrightness=findViewById(R.id.sbBrightness);
        spPresets=findViewById(R.id.spPresets);
        btnSendSOS.setOnClickListener(new ButtonListener());
        btnSendOK.setOnClickListener(new ButtonListener());
        btnStartReading.setOnClickListener(new ButtonListener());
        btnStopReading.setOnClickListener(new ButtonListener());
        sbBrightness.setOnSeekBarChangeListener(new SeekBarListener());
        spPresets.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, presets));
        spPresets.setOnItemSelectedListener(new SpinnerListener());
        switchSystemBrightness.setOnCheckedChangeListener(new SwitchListener());
    }

    private void initCamera() {
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            if (cameraManager != null) {
                cameraID = cameraManager.getCameraIdList()[0];
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initScreenBrightness() {
        contentResolver = getContentResolver();
        window = getWindow();
        try {
            int lightLevel = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
            sbBrightness.setProgress(lightLevel);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void checkSensor(){
        isLightSensorPresent = !isNull(mLightSensor=mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        isProximitySensorPresent = !isNull(mProximitySensor=mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
    }

    private void startListener(){
        if(isLightSensorPresent) {
            mSensorManager.registerListener(mSensorEventListener, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "Registered Light Sensor", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Light Sensor not available", Toast.LENGTH_SHORT).show();
        if(isProximitySensorPresent){
            mSensorManager.registerListener(mSensorEventListener, mProximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "Registered Proximity Sensor", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Proximity Sensor not available", Toast.LENGTH_SHORT).show();
    }

    private void unregisterListener(){
        mSensorManager.unregisterListener(mSensorEventListener);
        Toast.makeText(this, "Unregister listener", Toast.LENGTH_SHORT).show();
        Log.d("Test", "Unregister listener" );
    }

    private void toggleFlashlight(final boolean toggleFlashlight){
        if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
            try { cameraManager.setTorchMode(cameraID, toggleFlashlight);
            } catch (CameraAccessException e) { e.printStackTrace(); }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toggleFlashlight) tvFlashlight.setBackgroundColor(Color.YELLOW);
                else tvFlashlight.setBackgroundColor(Color.WHITE);
            }
        });
    }

    private void changeLightLevel(float brightness) {
        this.brightnessLevel = brightness;
        if (switchSystemBrightness.isChecked()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    startActivity(i);
                } else {
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, (int) (brightnessLevel * 255));
                    Log.d("Brightness", brightnessLevel+"");
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, WRITE_SETTINGS);
                } else {
                    Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, (int) (brightnessLevel * 255));
                }
            }
        }
        else {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = brightnessLevel;
            window.setAttributes(layoutParams);
            Log.d("Brightness", brightnessLevel+"");
        }
    }

    private void sendMorseCode(final String morseCode){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("SOS", morseCode);
                    sending=true;
                    for (char c : morseCode.toCharArray()) {
                        toggleFlashlight(true);
                        if (c == '.') Thread.sleep(MORSE_UNIT);
                        else if (c == '-') Thread.sleep(MORSE_UNIT * 3);
                        toggleFlashlight(false);
                        Thread.sleep(MORSE_UNIT);
                    }
                    sending=false;
                }catch (InterruptedException e){
                    Log.d("EXCEPTION", e.getMessage());
                }
            }
        });
        thread.start();
    }

    private class SensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()){
                case Sensor.TYPE_LIGHT:
                    if(reading) {
                        lightLevel = event.values[0];
                        if (lightLevel > 100) {
                            currentState = LIGHT_HIGH_STATE;
                            if (lastState!=currentState)
                                signalMillisStart = System.currentTimeMillis();
                        }
                        else {
                            currentState = LIGHT_LOW_STATE;
                            signalMillisEnd = System.currentTimeMillis();
                            long time = signalMillisEnd - signalMillisStart;
                            if (lastState!=currentState) {
                                Log.d("lastState!=currentState", time + "");
                                if (time < MORSE_UNIT) morseCode.append('.');
                                else if (time < MORSE_UNIT * 3) morseCode.append('-');
                            }
                            else if (time < MORSE_UNIT * 3 && time > MORSE_UNIT*2){
                                Log.d("lastState==currentState", time + "");
                                morseCode.append("/");
                                Log.d("test", morseCode.toString());
                            }
                        }
                        lastState = currentState;
                    }
                    break;
                case Sensor.TYPE_PROXIMITY: if(!sending)toggleFlashlight(event.values[0]<PROXIMITY_SENSOR_THRESHOLD); break;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btnSendSOS)
                sendMorseCode(MorseCode.toMorse("SOS"));
            if(v.getId()==R.id.btnSendOK)
                sendMorseCode(MorseCode.toMorse("OK"));
            if(v.getId()==R.id.button) {
                morseCode.replace(0, morseCode.length(), "");
                reading = true;
            }
            if(v.getId()==R.id.button2) {
                reading = false;
                Log.d("Test", morseCode.toString());
                Log.d("Test", MorseCode.parse(morseCode.toString()));
                tvMorseCode.setText(MorseCode.parse(morseCode.toString()));
            }
        }
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener{
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            float value = (float)seekBar.getProgress()/100;
            float min = (float)presetValues[0];
            float max = (float)presetValues[1];
            changeLightLevel((value*(max-min)+min));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d("test", presets[position]);
            presetValues[0] = 0.2*position;
            presetValues[1] = 0.2*position+0.2;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
                Toast.makeText(getApplicationContext(), "System Brightness is ON", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "System Brightness is OFF", Toast.LENGTH_SHORT).show();

        }
    }
}
