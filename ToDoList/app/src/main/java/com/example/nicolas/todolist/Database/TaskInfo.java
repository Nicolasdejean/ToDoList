package com.example.nicolas.todolist.Database;

import android.provider.BaseColumns;

/**
 * Created by nicolas on 03/02/2018.
 */

public class TaskInfo {
    public static final String DB_NAME = "com.example.nicolas.myapplication.Database";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_CONTENT = "content";
        public static final String COL_TASK_DATE_YEAR = "year";
        public static final String COL_TASK_DATE_MONTH = "month";
        public static final String COL_TASK_DATE_DAY = "day";
        public static final String COL_TASK_DATE_HOUR = "hour";
        public static final String COL_TASK_DATE_MINUTES = "minutes";
    }
}
