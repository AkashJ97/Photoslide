package org.world.asa.photoslide;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
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

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener , SensorEventListener {


    Handler mHandler = new Handler();
    long startTime;
    long elapsedTime;
    String minutes, seconds, txtFromSpinner;
    long secs, mins;
    ImageView img;
    Spinner tra;
    SensorManager s;
    Sensor prox;
    Button en, dis;
    int i=0, j = 0;
    MediaPlayer song;
    boolean isplaying ;


    int[] carArray = {R.drawable.car_1, R.drawable.car_2, R.drawable.car_3, R.drawable.car_4, R.drawable.car_5, R.drawable.car_6, R.drawable.car_7, R.drawable.car_8};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tra = (Spinner) findViewById(R.id.track);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Track, R.layout.support_simple_spinner_dropdown_item);
        tra.setAdapter(adapter);
        tra.setOnItemSelectedListener(this);

        dis = (Button) findViewById(R.id.disable);
        dis.setClickable(false);



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        txtFromSpinner = tra.getSelectedItem().toString();


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

        ((TextView) findViewById(R.id.timer)).setText(minutes + " : " + seconds);


    }

    Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this, 0);
        }
    };

    Runnable runcars = new Runnable() {

        public void run() {

            if(i<carArray.length) {
                img.setImageResource(carArray[i]);
                i++;
                mHandler.postDelayed(this, 3000);
            }
            else
            {

                mHandler.removeCallbacks(runcars);
                mHandler.removeCallbacks(startTimer);
            }
        }
    };


    public void slideShowClick(View view) {

        i = 0;

        img = (ImageView) findViewById(R.id.imageView2);


        mHandler.removeCallbacks(runcars);
        mHandler.removeCallbacks(startTimer);
        startTime = System.currentTimeMillis();

        mHandler.postDelayed(startTimer, 0);


        mHandler.postDelayed(runcars, 0);


    }


    public void stopClick(View view) {

       if(isplaying) {
           song.stop();
           song.reset();
       }

    }

    public void playClick(View view) {


        if (txtFromSpinner.equals("Track 1")) {

            if(isplaying)
                song.reset();
            song = MediaPlayer.create(getApplicationContext(), R.raw.games_of_thrones);
            song.start();
            isplaying = true;


        } else if (txtFromSpinner.equals("Track 2")) {

            if(isplaying)
                song.reset();
            song = MediaPlayer.create(getApplicationContext(), R.raw.kabali_theme_ringtone);
            song.start();
            isplaying = true;


        } else if (txtFromSpinner.equals("Track 3")) {

            if(isplaying)
                song.reset();
            song = MediaPlayer.create(getApplicationContext(), R.raw.see_you_again);
            song.start();
            isplaying = true;


        } else {

            if(isplaying)
               song.reset();
            song = MediaPlayer.create(getApplicationContext(), R.raw.super_ringtone);
            song.start();
            isplaying = true;


        }

    }

    public void proximity(View v) {


        en  = (Button) findViewById(R.id.enable);
        dis = (Button) findViewById(R.id.disable);

        if (v.getId() == R.id.enable) {

            en.setClickable(false);
            dis.setClickable(true);
            mHandler.removeCallbacks(startTimer);
            mHandler.removeCallbacks(runcars);

            s = (SensorManager) getSystemService(SENSOR_SERVICE);
            prox = s.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            s.registerListener(this, prox , SensorManager.SENSOR_DELAY_NORMAL);


        }
        if (v.getId() == R.id.disable) {

            dis.setClickable(false);
            en.setClickable(true);
            s.unregisterListener(this);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.values[0] == 0) {


            mHandler.removeCallbacks(startTimer);
            j = j % carArray.length ;
            if (j < carArray.length) {
                img.setImageResource(carArray[j]);
                j++;
            }


        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

