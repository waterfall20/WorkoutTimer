package com.example.workouttimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.*;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CountDownTimer extends AppCompatActivity implements Workout.Listener {
    private TextView setTextView, countTextView, statusTextView, lefttimeTextView;
    private ProgressBar progressBar;
    private Button startStopButton, resetButton;
    private View.OnClickListener onStartListener, onStopListener;
    private Vibrator vib;
    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int sound,id;
    private Workout workout;
    private int count2, count1;
    private boolean isSound, isVib;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_timer); //activity_main になっていた

        SharedPreferences switchspf = PreferenceManager.getDefaultSharedPreferences(this);

        if (switchspf.getBoolean("sound", false)) isSound = true;
        else isSound = false;

        if (switchspf.getBoolean("viblation", false)) isVib = true;
        else isVib = false;

        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();
        sound = soundPool.load(this,R.raw.decision22,1);

        //データベースからトレーニングの中身を取得する
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        //Log.d("countdowntimer-id", String.valueOf(id));
        workout = getWorkout(id);

        setTextView = findViewById(R.id.setNumber);
        countTextView = findViewById(R.id.countNumber);
        statusTextView = findViewById(R.id.status);
        lefttimeTextView = findViewById(R.id.timeLeft);

        progressBar = findViewById(R.id.pb);

        startStopButton = findViewById(R.id.button_start_stop);

        resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(workout);
            }
        });

        onStartListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workout.start(CountDownTimer.this);
            }
        };
        onStopListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workout.stop();
            }
        };

        reset(workout);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private Workout getWorkout(int id) {
    try (Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM WORKOUTLIST WHERE id=" + id)) {
      while(cursor.moveToNext()) {
        String name = cursor.getString(1);
        int workout_time = cursor.getInt(2);
        int rest_time = cursor.getInt(3);
        int set_count = cursor.getInt(4);
        int set_number = cursor.getInt(5);
        int set_during = cursor.getInt(6);
        return new Workout(name,workout_time,rest_time,set_count,set_number,set_during);
      }
    }
    throw new IllegalArgumentException("データがありません. id="+id);
    }

    private void showWorkout(WorkoutProperties properties) {
        countTextView.setText(""+properties.getCount());
        setTextView.setText(""+properties.getSet());
        statusTextView.setText(""+properties.getStatus());

        if(progressBar.getMax() != properties.getTickMax()){
            progressBar.setMax(properties.getTickMax());
        }
        progressBar.setProgress(properties.getTickMax() - properties.getTick());
        int left = properties.getLeftSecond();
        int minute = left / 60;
        int second = left % 60;
        //Log.d("time is", String.valueOf(second));
        lefttimeTextView.setText(String.format((minute>0?"%1$02d:":"")+"%2$02d", minute, second));

        if (isSound) {
            if (second == 2) {
                count2++;
                if (count2 == 1) {
                    soundPool.play(sound, 1.0f, 1.0f, 0, 0, 1);
                    //Log.d("sound", "soundPool-2実行");
                }
            }

            if (second == 1) {
                count1++;
                if (count1 == 1) {
                    soundPool.play(sound, 1.0f, 1.0f, 0, 0, 1);
                    //Log.d("sound", "soundPool-1実行");
                }
                if (count1 == 20) {
                    soundPool.play(sound, 1.0f, 1.0f, 0, 0, 1);
                   // Log.d("sound", "soundPool-0実行");
                    if (isVib) {
                            vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                            vib.vibrate(500);
                           // Log.d("viblation", "バイブレーション起動");
                        }
                    count2 = count1 = 0;
                }
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void reset(Workout workout) {
        workout.reset();
        showWorkout(workout);

        setStart();
        startStopButton.setEnabled(true);
        resetButton.setEnabled(false);
    }

    @Override
    public void start(WorkoutProperties properties) {
        setStop();
        startStopButton.setEnabled(true);
        resetButton.setEnabled(false);
    }

    @Override
    public void progress(WorkoutProperties properties) {
        showWorkout(properties);
    }

    @Override
    public void finish(WorkoutProperties properties) {
        showWorkout(properties);
        startStopButton.setEnabled(false);
        resetButton.setEnabled(true);
    }

    @Override
    public void stop(WorkoutProperties properties) {
        setRestart();
        startStopButton.setEnabled(true);
        resetButton.setEnabled(true);
    }


    private void setStart() {
        startStopButton.setText("Start");
        startStopButton.setOnClickListener(onStartListener);
    }
    private void setRestart() {
        startStopButton.setText("Restart");
        startStopButton.setOnClickListener(onStartListener);
    }
    private void setStop() {
        startStopButton.setText("Stop");
        startStopButton.setOnClickListener(onStopListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        workout.stop();
        //Log.d("TAG", "onPause() called");

    }
}