package com.example.workouttimer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WorkoutDB";

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

        //create　table するとき使う
    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }
        //新規データを登録するときに使う
    public void insertData(String name, int workout_time, int rest_time, int set_count, int number, int set_during) {
        SQLiteDatabase database = getWritableDatabase();

        //この?に値を格納するには、SQLiteDatabase#compileStatementの戻り値であるSQLiteStatementのbindXxxメソッドを使用する
        String sql = "INSERT INTO WORKOUTLIST VALUES(NULL, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,name);//1個目の？にnameを渡す
        statement.bindLong(2,workout_time);
        statement.bindLong(3,rest_time);
        statement.bindLong(4,set_count);
        statement.bindLong(5,number);
        statement.bindLong(6,set_during);
        //executeInsertを使用すると戻り値で自動生成されたシーケンス値が取得できる。ログにキー項目を出力したり、同一キーで関連テーブルを更新する場合等に使用できる。
        statement.executeInsert();
    }
        //
    public Cursor getData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        //database.rawQuery("SQL文","SQL文中の?に置き換わる値")
        return database.rawQuery(sql,null);
    }

    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM WORKOUTLIST WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);
        statement.execute();
        database.close();
    }
    //ダイアログによるアップデート
    public void updateData(String name,  int work_time, int rest_time, int set_count, int number, int set_during, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE WORKOUTLIST SET name=?, work_time=?, rest_time=?, set_count=?, number=?, set_during=? WHERE id=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1,name);
        statement.bindLong(2,work_time);
        statement.bindLong(3,rest_time);
        statement.bindLong(4,set_count);
        statement.bindLong(5,number);
        statement.bindLong(6,set_during);
        statement.bindLong(7,id);

        statement.execute();
        database.close();
    }
}
