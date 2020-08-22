package com.example.workouttimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static SQLiteHelper mSQLiteHelper;
    Toolbar toolbar;
    private Button btnStart, btnList;
    TextView mainName;
    int id;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnStart = findViewById(R.id.btnStart);
        btnList = findViewById(R.id.btnList);
        mainName = findViewById(R.id.mainName);

        SharedPreferences sph = getSharedPreferences("DataSave",Context.MODE_PRIVATE);
        id = sph.getInt("id",-1);
       // Log.d("onCreate-id", String.valueOf(id));
        mainName.setText(sph.getString("name","トレーニング一覧へ"));

        mSQLiteHelper = new SQLiteHelper(this);
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS WORKOUTLIST (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR," +
                "work_time INTEGER, rest_time INTEGER, number INTEGER," +
                "set_count INTEGER, set_during INTEGER)");

        btnStart.setOnClickListener(this);
        btnList.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sph = getSharedPreferences("DataSave",Context.MODE_PRIVATE);
        id = sph.getInt("id",-1);
        //Log.d("onCreate-id", String.valueOf(id));
        mainName.setText(sph.getString("name","トレーニング一覧へ"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
               // Log.d("id", String.valueOf(id));
                if (id != -1) {
                    Intent intentStart = new Intent(MainActivity.this, CountDownTimer.class);
                    intentStart.putExtra("id", id);
                    startActivity(intentStart);
                }
                break;
            case R.id.btnList:
                Intent intentList = new Intent(MainActivity.this, WorkoutListActivity.class);
                startActivityForResult(intentList, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            id = data.getIntExtra("id", -1);//-1が渡されたらerror
            name = data.getStringExtra("name");
            mainName.setText(name);
            SharedPreferences spf = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spf.edit();
            editor.clear();
            editor.commit();
            editor.putString("name",name);
            editor.putInt("id",id);
            //Log.d("getId", String.valueOf(id));
            editor.apply();
            Toast ts = Toast.makeText(MainActivity.this, String.format("【%s】がセットしました。", name), Toast.LENGTH_SHORT);
            ts.setGravity(Gravity.TOP, 0, 160);
            ts.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnSetting) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", id);
        outState.putString("name", name);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mainName.setText(savedInstanceState.getString("name"));
        id = savedInstanceState.getInt("id");
    }
}