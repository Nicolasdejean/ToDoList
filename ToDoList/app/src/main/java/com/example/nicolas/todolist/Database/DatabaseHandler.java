package com.example.nicolas.todolist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by nicolas on 03/02/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context, TaskInfo.DB_NAME, null, TaskInfo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskInfo.TaskEntry.TABLE + " ( " +
                TaskInfo.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskInfo.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL, " +
                TaskInfo.TaskEntry.COL_TASK_CONTENT + " TEXT NOT NULL, " +
                TaskInfo.TaskEntry.COL_TASK_DATE_YEAR + " TEXT NOT NULL, " +
                TaskInfo.TaskEntry.COL_TASK_DATE_MONTH + " TEXT NOT NULL, " +
                TaskInfo.TaskEntry.COL_TASK_DATE_DAY + " TEXT NOT NULL, " +
                TaskInfo.TaskEntry.COL_TASK_DATE_HOUR + " TEXT NOT NULL, " +
                TaskInfo.TaskEntry.COL_TASK_DATE_MINUTES + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskInfo.TaskEntry.TABLE);
        onCreate(db);
    }

    public void addTask(String string) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskInfo.TaskEntry.COL_TASK_TITLE, string);
        values.put(TaskInfo.TaskEntry.COL_TASK_CONTENT, "empty");
        values.put(TaskInfo.TaskEntry.COL_TASK_DATE_YEAR, "-1");
        values.put(TaskInfo.TaskEntry.COL_TASK_DATE_MONTH, "-1");
        values.put(TaskInfo.TaskEntry.COL_TASK_DATE_DAY, "-1");
        values.put(TaskInfo.TaskEntry.COL_TASK_DATE_HOUR, "-1");
        values.put(TaskInfo.TaskEntry.COL_TASK_DATE_MINUTES, "-1");
        db.insert(TaskInfo.TaskEntry.TABLE,
                null,
                values);
        db.close();
    }

    public ArrayList<String> getAllTask() {
        ArrayList<String> tasks = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TaskInfo.TaskEntry.TABLE,
                new String[]{TaskInfo.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskInfo.TaskEntry.COL_TASK_TITLE);
            tasks.add(cursor.getString(idx));
        }
        cursor.close();
        return tasks;
    }

    public void deleteTask(String string) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskInfo.TaskEntry.TABLE,
                TaskInfo.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{string});
        db.close();
    }


    public String getData(String string, String dataInfo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TaskInfo.TaskEntry.TABLE,
                new String[]{TaskInfo.TaskEntry.COL_TASK_TITLE, dataInfo},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int idx_title = cursor.getColumnIndex(TaskInfo.TaskEntry.COL_TASK_TITLE);
            int idx_data = cursor.getColumnIndex(dataInfo);
            if (Objects.equals(cursor.getString(idx_title), string)) {
                String data = cursor.getString(idx_data);
                cursor.close();
                if (Objects.equals(data, "-1"))
                    return null;
                return data;
            }
        }
        cursor.close();
        return null;
    }

  public void updateDb(String id, String title, String content,
                       String year, String month, String day,
                       String hours, String minutes) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.query(TaskInfo.TaskEntry.TABLE,
                new String[]{TaskInfo.TaskEntry._ID},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx_id = cursor.getColumnIndex(TaskInfo.TaskEntry._ID);
            if (Objects.equals(cursor.getString(idx_id), id))
            {
                Log.wtf("ID", id);
                values.put(TaskInfo.TaskEntry.COL_TASK_TITLE, title);
                values.put(TaskInfo.TaskEntry.COL_TASK_CONTENT, content);
                values.put(TaskInfo.TaskEntry.COL_TASK_DATE_YEAR, year);
                values.put(TaskInfo.TaskEntry.COL_TASK_DATE_MONTH, month);
                values.put(TaskInfo.TaskEntry.COL_TASK_DATE_DAY, day);
                values.put(TaskInfo.TaskEntry.COL_TASK_DATE_HOUR, hours);
                values.put(TaskInfo.TaskEntry.COL_TASK_DATE_MINUTES, minutes);
                db.update(TaskInfo.TaskEntry.TABLE,
                        values,
                        id,
                        null);
            }
        }
    }


    public boolean TaskExist(String string) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TaskInfo.TaskEntry.TABLE,
                new String[]{TaskInfo.TaskEntry._ID, TaskInfo.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskInfo.TaskEntry.COL_TASK_TITLE);
            if (Objects.equals(cursor.getString(idx), string))
                return true;
        }
        if (Objects.equals("", string))
            return true;
        return false;
    }
}
