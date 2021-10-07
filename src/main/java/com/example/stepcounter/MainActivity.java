package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView vartv;
    private double varmagprev = 0;
    private Integer total_stepscount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vartv = findViewById(R.id.textviewelement);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent!= null){
                    float xaxisval = sensorEvent.values[0];
                    float yaxisval = sensorEvent.values[1];
                    float zaxisval = sensorEvent.values[2];

                    double varmag = Math.sqrt(xaxisval*xaxisval + yaxisval*yaxisval + zaxisval*zaxisval);
                    double varmagd = varmag - varmagprev;
                    varmagprev = varmag;



                    if (varmagd > 6){
                        total_stepscount++;
                    }
                    vartv.setText(total_stepscount.toString());
                }
            }



            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", total_stepscount);
        editor.apply();
    }

    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("stepCount", total_stepscount);
        editor.apply();
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        total_stepscount = sharedPreferences.getInt("stepCount", 0);
    }
}