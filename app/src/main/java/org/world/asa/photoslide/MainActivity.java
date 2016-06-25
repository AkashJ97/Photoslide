package org.world.asa.photoslide;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener , SensorEventListener{


    Handler mHandler = new Handler();
    long startTime;
    long elapsedTime;
    String minutes, seconds ,txtFromSpinner;
    long secs, mins;
    boolean stopped = false;
    ImageView img;
    Spinner tra;
    SensorManager s;
    Sensor prox;
    Button en , dis ;
    int j=0;




    int []carArray={R.drawable.car_1,R.drawable.car_2,R.drawable.car_3,R.drawable.car_4,R.drawable.car_5,R.drawable.car_6,R.drawable.car_7,R.drawable.car_8};
    int []bikeArray={R.drawable.bike_1,R.drawable.bike_2,R.drawable.bike_3,R.drawable.bike_4,R.drawable.bike_5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tra = (Spinner) findViewById(R.id.track);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Track, R.layout.support_simple_spinner_dropdown_item);
        tra.setAdapter(adapter);
        tra.setOnItemSelectedListener(this);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        txtFromSpinner = tra.getSelectedItem().toString();
        img=(ImageView)findViewById(R.id.imageView2);
        if(txtFromSpinner.equals("Cars")) {
            img.setImageResource(carArray[0]);
            mHandler.removeCallbacks(runbikes);
            mHandler.removeCallbacks(runcars);
            mHandler.removeCallbacks(startTimer);

        }
        else {
            img.setImageResource(bikeArray[0]);

            img.setImageResource(carArray[0]);
            mHandler.removeCallbacks(runcars);
            mHandler.removeCallbacks(runbikes);
            mHandler.removeCallbacks(startTimer);

        }


    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }



    private void updateTimer(float time) {
        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }
		/* Convert the minutes to String and format the String */
        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

        ((TextView) findViewById(R.id.timer)).setText( minutes + " : " + seconds);

    }

    Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this, 10);
        }
    };

    Runnable runcars = new Runnable() {
        int i=0;
        public void run() {

            if(i<carArray.length) {
                img.setImageResource(carArray[i]);
                i++;
                mHandler.postDelayed(this, 3000);
            }
            else
            {

                mHandler.removeCallbacks(runcars);
                mHandler.removeCallbacks(runbikes);
                mHandler.removeCallbacks(startTimer);
            }
        }
    };

    Runnable runbikes = new Runnable() {
        int i=0;
        public void run() {

            if(i<bikeArray.length) {
                img.setImageResource(bikeArray[i]);
                i++;
                mHandler.postDelayed(this, 3000);
            }
            else
            {

                mHandler.removeCallbacks(runcars);
                mHandler.removeCallbacks(runbikes);
                mHandler.removeCallbacks(startTimer);
            }
        }
    };








    public void slideShowClick(View view) {

        img=(ImageView)findViewById(R.id.imageView2);
        txtFromSpinner = tra.getSelectedItem().toString();
        if(txtFromSpinner.equals("Cars")) {

            startTime = System.currentTimeMillis();

            mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 10);

            mHandler.removeCallbacks(runcars);
            mHandler.postDelayed(runcars, 0);
        }
        else
        {

            startTime = System.currentTimeMillis();

            mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 10);

            mHandler.removeCallbacks(runbikes);
            mHandler.postDelayed(runbikes, 0);
        }


    }




    public void stopClick(View view) {


        mHandler.removeCallbacks(startTimer);
        txtFromSpinner = tra.getSelectedItem().toString();
        stopped = true;
        mHandler.removeCallbacks(runcars);
        mHandler.removeCallbacks(runbikes);

    }

    public void playClick(View view) {


        img=(ImageView)findViewById(R.id.imageView2);
        if(txtFromSpinner.equals("Cars")) {
            startTime = System.currentTimeMillis() - elapsedTime;
            mHandler.postDelayed(runcars, 0 );
            mHandler.postDelayed(startTimer, 10);
        }
        else
        {
            startTime = System.currentTimeMillis() - elapsedTime;
            mHandler.postDelayed(runbikes, 3000);
            mHandler.postDelayed(startTimer, 10);
        }

    }

    public void proximity(View v) {

        en = (Button)findViewById(R.id.enable);
        dis = (Button)findViewById(R.id.disable);

        if(v.getId() == R.id.enable)
        {
            j=0;
            en.setClickable(false);
            dis.setClickable(true);

            s = (SensorManager)getSystemService(SENSOR_SERVICE);
            prox = s.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            s.registerListener(this , prox , SensorManager.SENSOR_DELAY_NORMAL ) ;


        }
        if(v.getId() == R.id.disable)
        {
            j=0;
            dis.setClickable(false);
            en.setClickable(true);
            s.unregisterListener(this);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.values[0] == 0) {
            if (txtFromSpinner.equals("Cars")) {
                mHandler.removeCallbacks(runbikes);
                mHandler.removeCallbacks(runcars);
                mHandler.removeCallbacks(startTimer);
                if (j < carArray.length) {
                    img.setImageResource(carArray[j]);
                    j++;
                }


            } else {
                mHandler.removeCallbacks(runbikes);
                mHandler.removeCallbacks(runcars);
                mHandler.removeCallbacks(startTimer);
                if (j < bikeArray.length) {
                    img.setImageResource(bikeArray[j]);
                    j++;
                }

            }
        }

    }





    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}