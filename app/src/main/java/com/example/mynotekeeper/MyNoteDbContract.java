package com.example.mynotekeeper;

import android.provider.BaseColumns;

public final class MyNoteDbContract {
    private MyNoteDbContract(){}



    public static final class CourseEntryInfo implements BaseColumns {
        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_COURSE_TITLE = "course_title";

        //CREATE COURSE_INFO TABLE(course_id, course_title);
        public static final String CREATE_TABLE =
                " CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY, "
                        + COLUMN_COURSE_ID + " TEXT UNIQUE NOT NULL,"
                        + COLUMN_COURSE_TITLE + " TEXT NOT NULL )";

        public static final String getQName(String columnName) {
            return TABLE_NAME + "." + columnName;
        }

    }

    public static final class NoteInfoEntry implements BaseColumns{
        public static final String TABLE_NAME = "note_info";
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_COURSE_ID = "course_id";

        //create table note_info (note_id, note, course_id)
        public static final String CREATE_TABLE =
                " CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY, "
                        + COLUMN_NOTE_TITLE + " TEXT NOT NULL, "
                        + COLUMN_NOTE + " TEXT, "
                        + COLUMN_COURSE_ID + " TEXT NOT NULL )";

        public static final String getQName(String columnName) {
            return TABLE_NAME + "." + columnName;
        }
    }
}
