package com.example.nicolas.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nicolas.todolist.Database.DatabaseHandler;
import com.example.nicolas.todolist.Database.TaskInfo;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    public static DatabaseHandler db;
    private ListView taskList;
    private ArrayAdapter<String> ad;
    public static final String EDIT_TASK = "EDIT";
    public static final String VIEW_TASK = "VIEW";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.wtf("main", "before database");
        db = new DatabaseHandler(this);
        Log.wtf("main", "after database");

        taskList = (ListView) findViewById(R.id.items_list);


        SQLiteDatabase dbs = db.getReadableDatabase();
        Cursor cursor = dbs.query(TaskInfo.TaskEntry.TABLE,
                new String[]{TaskInfo.TaskEntry._ID, TaskInfo.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskInfo.TaskEntry.COL_TASK_TITLE);
            Log.d("mainactivity", "Task: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task: {
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add a new task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                if (!db.TaskExist(task)) {
                                    db.addTask(task);
                                }
                                updateUI();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        db.deleteTask(task);
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> tasks = db.getAllTask();

        if (ad == null) {
            ad = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    tasks);
            taskList.setAdapter(ad);
        } else {
            ad.clear();
            ad.addAll(tasks);
            ad.notifyDataSetChanged();
        }

        db.close();
    }
    public void editTask(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        intent.putExtra(EDIT_TASK, task);
        startActivity(intent);

    }

    public void viewTask(View view) {
        Intent intent = new Intent(this, ContentActivity.class);
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        intent.putExtra(VIEW_TASK, task);
        startActivity(intent);

    }

}
