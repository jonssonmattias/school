package se.mau.mattiasjonsson.assignment1;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SensorActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private LinearLayout linearLayout;
    private ScrollView scrollView;
    private TableRow valuesTableRow, accuracyTableRow, timestampTableRow;
    private Button btnRegister, btnUnregister;
    private TextView tvTitle;
    private SensorManager sensorManager;
    private int sensorType;
    private Sensor sensor;
    private SensorEventListener sensorEventListener = new SensorListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        initComponents();
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorType = getIntent().getIntExtra("sensorType", -1);
        if(setSensor()) {
            ArrayList<String[]> list = setList(sensor);
            setTable(list);
            tvTitle.setText(sensor.getName());
        }
    }

    private void initComponents(){
        tvTitle = findViewById(R.id.tvTitle);
        linearLayout = findViewById(R.id.linearLayout);
        btnRegister = findViewById(R.id.btnRegister);
        btnUnregister = findViewById(R.id.btnUnregister);
        btnUnregister.setEnabled(false);
        btnRegister.setOnClickListener(new ButtonListener());
        btnUnregister.setOnClickListener(new ButtonListener());
        tableLayout = new TableLayout(this);
        scrollView = new ScrollView(this);
        scrollView.addView(tableLayout);
        linearLayout.addView(scrollView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
    }

    @Override protected void onPause() {
        super.onPause();
        unregisterListener();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        unregisterListener();
        sensorManager = null;
        sensor = null;
    }

    private boolean setSensor(){
        if(sensorManager.getDefaultSensor(sensorType)!=null){
            sensor = sensorManager.getDefaultSensor(sensorType);
            Toast.makeText(getApplicationContext(),sensor.getStringType()+" exists",Toast.LENGTH_LONG).show();
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(),sensor.getStringType()+" does not exists",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void registerListener(){
        sensorManager.registerListener(sensorEventListener, sensor,SensorManager.SENSOR_DELAY_NORMAL);
        btnUnregister.setEnabled(true);
        btnRegister.setEnabled(false);
        Toast.makeText(getApplicationContext(),"Registered listener",Toast.LENGTH_LONG).show();
    }

    private void unregisterListener(){
        sensorManager.unregisterListener(sensorEventListener);
        btnUnregister.setEnabled(false);
        btnRegister.setEnabled(true);
        Toast.makeText(getApplicationContext(),"Unregister listener",Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String[]> setList(Sensor sensor) {
        ArrayList<String[]> sensorList = new ArrayList<>();
        sensorList.add(new String[]{"Type", sensor.getStringType()});
        sensorList.add(new String[]{"Vendor", sensor.getVendor()});
        sensorList.add(new String[]{"FifoMaxEventCount", Integer.toString(sensor.getFifoMaxEventCount())});
        sensorList.add(new String[]{"FifoReservedEventCount", Integer.toString(sensor.getFifoReservedEventCount())});
        sensorList.add(new String[]{"ID", Integer.toString(sensor.getId())});
        sensorList.add(new String[]{"MaxDelay", Integer.toString(sensor.getMaxDelay())});
        sensorList.add(new String[]{"MinDelay", Integer.toString(sensor.getMinDelay())});
        sensorList.add(new String[]{"Power", Float.toString(sensor.getPower())});
        sensorList.add(new String[]{"ReportingMode", Integer.toString(sensor.getReportingMode())});
        sensorList.add(new String[]{"Resolution", Float.toString(sensor.getResolution())});
        sensorList.add(new String[]{"Is additionalInfoSupported", Boolean.toString(sensor.isAdditionalInfoSupported())});
        sensorList.add(new String[]{"Is dynamicSensor", Boolean.toString(sensor.isDynamicSensor())});
        sensorList.add(new String[]{"Is wakeUpSensor", Boolean.toString(sensor.isWakeUpSensor())});
        if(Build.VERSION.SDK_INT >= 26)
            sensorList.add(new String[]{"HighestDirectReportRateLevel", Integer.toString(sensor.getHighestDirectReportRateLevel())});
        return sensorList;
    }

    private void setTable(ArrayList<String[]> sensorList) {
        TableRow tableRow = new TableRow(this);
        TextView tv1 = new TextView(this);
        tv1.setText(" Name ");
        tableRow.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" Value ");
        tableRow.addView(tv2);
        tableLayout.addView(tableRow);
        for (int i = 0; i < sensorList.size(); i++) {
            String key = sensorList.get(i)[0];
            String value = sensorList.get(i)[1];
            tableLayout.addView(getTableRow(key, value));
        }
        valuesTableRow = getTableRow("Values", "null");
        accuracyTableRow = getTableRow("Accuracy", "null");
        tableLayout.addView(accuracyTableRow);
        tableLayout.addView(valuesTableRow);
    }

    private TableRow getTableRow(String key, String value){
        TableRow tableRow = new TableRow(this);
        TextView t1v = new TextView(this);
        t1v.setText(key);
        tableRow.addView(t1v);

        TextView t2v = new TextView(this);
        t2v.setText(value);
        tableRow.addView(t2v);
        return tableRow;
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnRegister)
                registerListener();
            else if(v.getId() == R.id.btnUnregister)
                unregisterListener();
        }
    }

    private class SensorListener implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == sensor.getType()){
                StringBuilder values = new StringBuilder();
                for (int i = 0; i < event.values.length; i++){
                    values.append(event.values[i] + "\n");
                }
                tableLayout.removeView(timestampTableRow);
                timestampTableRow = getTableRow("Timestamp", Integer.toString((int)(event.timestamp*Math.pow(10,-9))));
                tableLayout.addView(timestampTableRow);
                tableLayout.removeView(accuracyTableRow);
                accuracyTableRow = getTableRow("Accuracy", Integer.toString(event.accuracy));
                tableLayout.addView(accuracyTableRow);
                tableLayout.removeView(valuesTableRow);
                valuesTableRow = getTableRow("Values", values.toString());
                tableLayout.addView(valuesTableRow);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
