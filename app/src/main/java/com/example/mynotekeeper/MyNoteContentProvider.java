package com.example.mynotekeeper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.mynotekeeper.MyNoteContentProviderContract.Notes;

import static com.example.mynotekeeper.MyNoteContentProviderContract.*;
import static com.example.mynotekeeper.MyNoteDbContract.*;

public class MyNoteContentProvider extends ContentProvider {

    private MyNoteOpenHelper mOpenHelper;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(Constants.AUTHORITY, Courses.PATH, Constants.COURSES);
        sUriMatcher.addURI(Constants.AUTHORITY, Notes.PATH, Constants.NOTES);
        sUriMatcher.addURI(Constants.AUTHORITY, Notes.PATH_EXTENDED, Constants.NOTES_EXPANDED);

    }
    public MyNoteContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
     mOpenHelper = new MyNoteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

       Cursor cursor = null;
       SQLiteDatabase db = mOpenHelper.getReadableDatabase();

       int uriMatch = sUriMatcher.match(uri);

       switch (uriMatch) {

           case Constants.COURSES:
               cursor = db.query(CourseEntryInfo.TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);

               break;

           case Constants.NOTES:
                   cursor = db.query(NoteInfoEntry.TABLE_NAME, projection, selection, selectionArgs,null, null, sortOrder);

                   break;

           case Constants.NOTES_EXPANDED:
               cursor = notesExpandedQuery(db, projection, selection, selectionArgs, sortOrder);

               break;

       }
        return cursor;

    }

    private Cursor notesExpandedQuery(SQLiteDatabase db, String[] projection,
                                      String selection, String[] selectionArgs, String sortOrder) {

        String[] columns = new String[projection.length];
        for (int idx=0; idx < projection.length; idx++){
            columns[idx] = projection[idx].equals(BaseColumns._ID) ||
                    projection[idx].equals(CourseColumnId.COLUMN_COURSE_ID) ?
                    NoteInfoEntry.getQName(projection[idx]) : projection[idx];
        }

        String tableWithJoin = NoteInfoEntry.TABLE_NAME + " JOIN " + CourseEntryInfo.TABLE_NAME + " ON "
                                + NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID) + "="
                                + CourseEntryInfo.getQName(CourseEntryInfo.COLUMN_COURSE_ID);

        return db.query(tableWithJoin, columns, selection, selectionArgs, null, null, sortOrder);


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
