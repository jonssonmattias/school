package se.mau.mattiasjonsson.assignment1;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private ListView sensorListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorListView = findViewById(R.id.sensorListView);
        setSensorList();
    }

    private void setSensorList(){
        List<String> sensorTitles = new ArrayList<>();
        for (Sensor s : sensorList)
            sensorTitles.add(s.getName());
        ArrayAdapter<String> sensorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,sensorTitles);
        sensorListView.setAdapter(sensorAdapter);
        sensorListView.setOnItemClickListener(new ItemListener());
    }

    private class ItemListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.d("test", sensorList.get(i).getName());
            Intent intent = new Intent(MainActivity.this, SensorActivity.class);
            intent.putExtra("sensorType", sensorList.get(i).getType());
            startActivity(intent);
        }
    }
}
