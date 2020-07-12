package com.example.mynotekeeper;

import android.net.Uri;

public class Constants {

    public  final String TAG = getClass().getSimpleName();
    public static final String  NOTE_INFO = "NOTE INFO";
    public static final int ID_NOT_SET = -1;
    public static final String COURSE_ID = "NOTE_ID";
    public static final String NOTE_TITLE = "NOTE_TITLE";
    public static final String NOTE_BODY ="NOTE_BODY";
    public static final String DATABASE_NAME = "MyNote.db";
    public static final int DATABASE_VERSION = 1;
    public static final int COURSE_LOADER = 1;
    public static final int NOTE_LOADER = 2;
    public static final String AUTHORITY = "com.example.mynotekeeper.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final int NOTES = 1;
    public static final int COURSES = 0;
    public static final int NOTES_EXPANDED = 2;

}
