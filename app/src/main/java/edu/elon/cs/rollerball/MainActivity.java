//(c) Patrick Lathan and Elena Sparacio//
//MainActivity for the Rollerball application that implements a SensorEventListener//
//and gets X,Y values for whenever the device is tilted.//

package edu.elon.cs.rollerball;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;

public class MainActivity extends Activity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor mAccel;
    public static float tiltX, tiltY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


    }

    @Override
    protected void onPause() {
        super.onPause();

        // Accelerometer
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Accelerometer
        sensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        tiltX = -1*(event.values[0]);
        tiltY = event.values[1];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}