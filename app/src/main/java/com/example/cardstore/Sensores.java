package com.example.cardstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.util.GeoPoint;


public class Sensores extends AppCompatActivity implements LocationListener {


    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    TextView textView1;
    TextView textView2;
    TextView textView3;

    TextView textView4;

    TextView textView5;

    Button button;
    String data;


    private LocationManager locationManager;
    int cont = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        textView1 = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);



        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float x = sensorEvent.values[0];
                    float y = sensorEvent.values[1];
                    float z = sensorEvent.values[2];
                    textView1.setText("x: " + String.valueOf(x));
                    textView2.setText("y: " + String.valueOf(y));


                    if (x < -4 && cont == 0) {
                        cont++;
                    }
                    if (x > 4 && cont == 1) {
                        cont++;
                    }
                    if (cont == 2) {

                        cont = 0;
                    }
                    if (ContextCompat.checkSelfPermission(Sensores.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Sensores.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                    }

                }


                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
        }
        start();
    }


    public void ubicacion(View view) {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            textView4.setText("Latidud: " + String.valueOf(location.getLatitude()));

            textView5.setText("Longitud: " + String.valueOf(location.getLongitude()));

        }

    }
    public void Inicio(View view) {
        // Execute the Atras method in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Simulate some background task if needed
                try {
                    Thread.sleep(2000); // Simulating a 2-second task
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Switch back to the main (UI) thread to start the MainActivity
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Sensores.this, MainActivity.class));
                    }
                });
            }
        }).start();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void start(){
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }



}