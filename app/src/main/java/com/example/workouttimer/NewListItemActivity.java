package com.example.workouttimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.workouttimer.MainActivity.mSQLiteHelper;

public class NewListItemActivity extends AppCompatActivity {

    EditText mEdtName, mEdtWorkoutTime, mEdtRestTime, mEdtSetCount, mEdtNumber, mEdtSetDuring;
    Button mBtnConfirm, mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list_item);

        //EditText
        mEdtName = findViewById(R.id.edtName);
        mEdtWorkoutTime = findViewById(R.id.edtWorkoutTime);
        mEdtRestTime = findViewById(R.id.edtRestTime);
        mEdtSetCount = findViewById(R.id.edtSetCount);
        mEdtNumber = findViewById(R.id.edtNumber);
        mEdtSetDuring = findViewById(R.id.edtSetDuring);

//        //creating database
//        //インスタンス化する
//        mSQLiteHelper = new SQLiteHelper(this);
//        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS WORKOUTLIST (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,"+
//                "work_time INTEGER, rest_time INTEGER, number INTEGER,"+
//                "set_count INTEGER, set_during INTEGER)");


        //新規作成追加
        //Button 追加
        mBtnConfirm = findViewById(R.id.btnConfirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mSQLiteHelper.insertData(
                            mEdtName.getText().toString().trim(),
                            Integer.parseInt(mEdtWorkoutTime.getText().toString().trim()),
                            Integer.parseInt(mEdtRestTime.getText().toString().trim()),
                            Integer.parseInt(mEdtSetCount.getText().toString().trim()),
                            Integer.parseInt(mEdtNumber.getText().toString().trim()),
                            Integer.parseInt(mEdtSetDuring.getText().toString().trim())
                    );
                    Toast.makeText(NewListItemActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    //reset views
                    mEdtName.setText("");
                    mEdtWorkoutTime.setText("");
                    mEdtRestTime.setText("");
                    mEdtSetCount.setText("");
                    mEdtNumber.setText("");
                    mEdtSetDuring.setText("");
                }
                catch(Exception e){
                    Toast.makeText(NewListItemActivity.this, "Added  Not successfully", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                setResult(RESULT_OK,new Intent());
                finish();
            }
        });

        mBtnCancel = findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
