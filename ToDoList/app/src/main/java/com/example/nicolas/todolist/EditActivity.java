package com.example.nicolas.todolist;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.nicolas.todolist.Database.TaskInfo;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    //private DatabaseHandler db;
    private DatePicker datePicker;
    private String _id;
    private String _day;
    private String _month;
    private String _year;
    private String _hour;
    private String _minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EDIT_TASK);

        _id = MainActivity.db.getData(message, TaskInfo.TaskEntry._ID);

        _year = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_YEAR);
        _month = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_MONTH);
        _day = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_DAY);

        _minutes = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_MINUTES);
        _hour = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_HOUR);

        TextView titleView = findViewById(R.id.edit_task);
        titleView.setText(message);

        TextView contentView = findViewById(R.id.edit_content);
        String content = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_CONTENT);

        if (content != null) {
            final EditText contentText = new EditText(this);
            contentText.setText(content, TextView.BufferType.EDITABLE);
            contentView.setText(content);
        }
        displayActualDate();
        displayActualTime();
    }

    public void displayActualDate() {
        TextView date = (TextView) findViewById(R.id.edit_date_title);
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (_day == null && _year == null && _month == null) {
            date.setText(new StringBuilder()
                    .append(month + 1).append("-").append(day).append("-")
                    .append(year).append(" "));
            _year = String.valueOf(year);
            _month = String.valueOf(month);
            _day = String.valueOf(day);
        } else {
            date.setText(new StringBuilder()
                    .append(Integer.parseInt(_month) + 1).append("-").append(_day).append("-")
                    .append(_year).append(" "));

        }
    }

    public void displayActualTime() {
        TextView time = (TextView) findViewById(R.id.edit_time_title);
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        if (_hour == null && _minutes == null) {
            if (minute < 10 && hour < 10)
                time.setText(new StringBuilder()
                        .append("0").append(hour).append(":0").append(minute));
            else if (minute < 10)
                time.setText(new StringBuilder()
                        .append(hour).append(":0").append(minute));
            else if (hour < 10)
                time.setText(new StringBuilder()
                        .append("0").append(hour).append(":").append(minute));
            else
                time.setText(new StringBuilder()
                        .append(hour).append(":").append(minute));

            _hour = String.valueOf(hour);
            _minutes = String.valueOf(minute);
        }
        else {
            if (Integer.parseInt(_minutes) < 10 && Integer.parseInt(_hour) < 10)
                time.setText(new StringBuilder()
                        .append(_hour).append(":0").append(_minutes));
            else if (Integer.parseInt(_minutes) < 10)
                time.setText(new StringBuilder()
                        .append(_hour).append(":0").append(_minutes));
            else if (Integer.parseInt(_hour) < 10)
                time.setText(new StringBuilder()
                        .append("0").append(_hour).append(":").append(_minutes));
            else
                time.setText(new StringBuilder()
                        .append(_hour).append(":").append(_minutes));

        }

    }

    public void changeDate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        datePicker = new DatePicker(this);
        datePicker.setCalendarViewShown(false);

        builder.setTitle("Choose the time for your task");
        builder.setView(datePicker);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                _day = String.valueOf(datePicker.getDayOfMonth());
                _month = String.valueOf(datePicker.getMonth());
                _year = String.valueOf(datePicker.getYear());
                displayActualDate();
            }
        });

        builder.show();
    }

    public void changeTime(View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.wtf("minutes", String.valueOf(selectedMinute));
                Log.wtf("hour", String.valueOf(selectedHour));
                _minutes = String.valueOf(selectedMinute);
                _hour = String.valueOf(selectedHour);
                displayActualTime();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void saveEdits(View view) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String title = ((EditText) findViewById(R.id.edit_task)).getText().toString();
        String content = ((EditText) findViewById(R.id.edit_content)).getText().toString();

        if (content == null)
            content = "";
        MainActivity.db.updateDb(_id, title, content,
                _year, _month, _day, _hour, _minutes);
        Log.wtf("title", title);
        Log.wtf("content", content);
        Log.wtf("hour", _hour);
        Log.wtf("minutes", _minutes);
        Log.wtf("Year", _year);
        Log.wtf("month", _month);
        Log.wtf("day", _day);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
