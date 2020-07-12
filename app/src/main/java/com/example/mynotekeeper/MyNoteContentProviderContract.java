package com.example.mynotekeeper;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MyNoteContentProviderContract {

    private MyNoteContentProviderContract(){}

    protected interface CourseColumnId{
        public static final String COLUMN_COURSE_ID = "course_id";
    }

    protected interface CoursesColumns{
        public static final String COLUMN_COURSE_TITLE = "course_title";
    }

    public interface NotesColumns{
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";
    }

    public static final class Courses implements BaseColumns, CoursesColumns,CourseColumnId {
        public static final String PATH = "courses";

        //content://com.example.mynotekeeper.provider/courses
        public static final Uri CONTENT_URI = Uri.withAppendedPath(Constants.AUTHORITY_URI, PATH);
    }

    public static final class Notes implements BaseColumns,NotesColumns,CourseColumnId,CoursesColumns{
        public static final String PATH = "notes";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(Constants.AUTHORITY_URI, PATH);

        public static final String PATH_EXTENDED = "notes_extended";
        public static final Uri CONTENT_EXPANDED_URI = Uri.withAppendedPath(Constants.AUTHORITY_URI, PATH_EXTENDED);
    }
}
