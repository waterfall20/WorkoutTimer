package com.example.workouttimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
//import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

import static com.example.workouttimer.MainActivity.mSQLiteHelper;


public class WorkoutListActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView mListView;
    WorkoutListAdapter mAdapter = null;
    ArrayList<Model> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = findViewById(R.id.workoutList);
        mList = new ArrayList<>();
        mAdapter = new WorkoutListAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //creating database
        //インスタンス化
//        mSQLiteHelper = new SQLiteHelper(this);
//
//        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS WORKOUTLIST (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR," +
//                "work_time INTEGER, rest_time INTEGER, number INTEGER," +
//                "set_count INTEGER, set_during INTEGER)");

        updateWorkoutList();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                createDialog(position);
            }
        });
    }

    private void createDialog(final int position) {
       new AlertDialog.Builder(WorkoutListActivity.this)
               .setTitle("行う操作を指定してください")
                .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDialogDelete(getId(position));
                    }
                }).setNegativeButton("編集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialogUpdate(WorkoutListActivity.this, getId(position));
            }
        }).setNeutralButton("セット", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendInformation(getId(position));
                //Log.d("position", String.valueOf(position));
            }
        }).show();
    }

    private Integer getId(int position){
        Cursor c = mSQLiteHelper.getData("SELECT id FROM WORKOUTLIST");
        ArrayList<Integer> arrID = new ArrayList<Integer>();
        while (c.moveToNext()) {
            arrID.add(c.getInt(0));
        }
        //Log.d("arrID.get(position)", String.valueOf(arrID.get(position)));
        return arrID.get(position);
    }

    private void sendInformation(Integer position) {
        Cursor cursor = mSQLiteHelper.getData("SELECT name FROM WORKOUTLIST WHERE id=" + position);
        String name = "";
        while(cursor.moveToNext()) {
        name = cursor.getString(0);
        }
        Intent i = new Intent();
        i.putExtra("id",position);
        i.putExtra("name", name);
        setResult(RESULT_OK, i);
        finish();
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(WorkoutListActivity.this);
        dialogDelete.setTitle("注意!");
        dialogDelete.setMessage("本当に削除しますか");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int id;
                    SharedPreferences spf = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    id = spf.getInt("id",-1);
                    if (idRecord == id) {
                        editor.clear();
                        editor.commit();
                        editor.putString("name", "トレーニング一覧へ");
                        editor.putInt("id", -1);
                        editor.apply();
                    }
                    mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(WorkoutListActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                  //  Log.e("error", e.getMessage());
                }
                updateWorkoutList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(final Activity activity, final Integer position) {

        //カスタムレイアウトの用意
        LayoutInflater layoutInflater = getLayoutInflater();
        View customAlertView = layoutInflater.inflate(R.layout.update_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customAlertView)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateWorkoutList();
                    }
                });

        final EditText edtName = customAlertView.findViewById(R.id.edtName);
        final EditText edtWorkoutTime = customAlertView.findViewById(R.id.edtWorkoutTime);
        final EditText edtRestTime = customAlertView.findViewById(R.id.edtRestTime);
        final EditText edtSetCount = customAlertView.findViewById(R.id.edtSetCount);
        final EditText edtNumber = customAlertView.findViewById(R.id.edtNumber);
        final EditText edtSetDuring = customAlertView.findViewById(R.id.edtSetDuring);
        Button btnUpdate = customAlertView.findViewById(R.id.btnUpdate);

        //get data of row clicked from sqlite
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM WORKOUTLIST WHERE id=" + position);
        mList.clear();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            edtName.setText(name); //set name to update dialog
            int workoutTime = cursor.getInt(2);
            edtWorkoutTime.setText(String.valueOf(workoutTime)); //set workoutTime to update dialog
            int restTime = cursor.getInt(3);
            edtRestTime.setText(String.valueOf(restTime)); //set restTime to update dialog
            int setCount = cursor.getInt(4);
            edtSetCount.setText(String.valueOf(setCount)); //set setCount to update dialog
            int number = cursor.getInt(5);
            edtNumber.setText(String.valueOf(number)); //set number to update dialog
            int setDuring = cursor.getInt(6);
            edtSetDuring.setText(String.valueOf(setDuring)); //set setDuring to update dialog
            // add to list
            mList.add(new Model(id, name, workoutTime, restTime, setCount, number, setDuring));
        }

        final AlertDialog alertDialog = builder.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSQLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            Integer.parseInt(edtWorkoutTime.getText().toString().trim()),
                            Integer.parseInt(edtRestTime.getText().toString().trim()),
                            Integer.parseInt(edtSetCount.getText().toString().trim()),
                            Integer.parseInt(edtNumber.getText().toString().trim()),
                            Integer.parseInt(edtSetDuring.getText().toString().trim()),
                            position
                    );
                    alertDialog.dismiss();
                    Toast.makeText(activity, "Update Successfull", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                   // Log.e("Update error", error.getMessage());
                }
                //updateWorkoutList();
            }
        });
        alertDialog.show();
    }

    /* get all data from sqlite
                  Cursor //1レコードずつ取り出す。任意のレコード場場指定はできない
                  SQLiteDatabaseのデータを取り出す際に使用します。
                  get all data from sqlite*/
    private void updateWorkoutList() {
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM WORKOUTLIST");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int workout_time = cursor.getInt(2);
            int rest_time = cursor.getInt(3);
            int set_count = cursor.getInt(4);
            int number = cursor.getInt(5);
            int set_during = cursor.getInt(6);
             /* add to list
               DBから取得した値をModel引数に渡し、mListに追加する */
            mList.add(new Model(id, name, workout_time, rest_time, set_count, number, set_during));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.btnAdd:
//                //新規作成
//                Intent intentAdd = new Intent(WorkoutListActivity.this, NewListItemActivity.class);
//                startActivityForResult(intentAdd,2);
//                break;
//            case R.id.btnDelete:
//                SparseBooleanArray checked = mListView.getCheckedItemPositions();
//                Toast.makeText(WorkoutListActivity.this, String.valueOf(checked.size()), Toast.LENGTH_SHORT).show();
//                for (int i = 1; i < checked.size(); i++) {
//                    if (checked.valueAt(i)){
//                        Log.d("checkbox",String.valueOf(checked.indexOfKey(i)));
//                        mSQLiteHelper.deleteData(checked.indexOfKey(i));
//                    }
//                }
//                updateWorkoutList();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ( mListView.getAdapter().getCount() == 0 ) {
            //Log.e("リストの要素数", String.valueOf(mListView.getAdapter().getCount()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnAdd) {
                //新規作成
                Intent intentAdd = new Intent(WorkoutListActivity.this, NewListItemActivity.class);
                startActivityForResult(intentAdd,2);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            updateWorkoutList();
        }
    }
}
