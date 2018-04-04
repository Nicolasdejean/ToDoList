package com.example.nicolas.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nicolas.todolist.Database.TaskInfo;

import java.util.Calendar;

public class ContentActivity extends AppCompatActivity {

    private String _id;
    private String _day;
    private String _month;
    private String _year;
    private String _hour;
    private String _minutes;
    private String _content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.VIEW_TASK);

        _id = MainActivity.db.getData(message, TaskInfo.TaskEntry._ID);

        _year = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_YEAR);
        _month = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_MONTH);
        _day = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_DAY);

        _minutes = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_MINUTES);
        _hour = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_DATE_HOUR);

        _content = MainActivity.db.getData(message, TaskInfo.TaskEntry.COL_TASK_CONTENT);

        displayTitle(message);
        displayContent();
        displayDate();
        displayTime();
    }

    public void displayTitle(String title) {
        TextView titleView = findViewById(R.id.content_task);
        titleView.setText(title);
    }

    public void displayContent() {
        TextView contentView = findViewById(R.id.content_content);
        contentView.setText(_content);
    }

    public void displayDate() {
        TextView date = (TextView) findViewById(R.id.content_date_title);
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

    public void displayTime() {
        TextView time = (TextView) findViewById(R.id.content_time_title);
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

}
