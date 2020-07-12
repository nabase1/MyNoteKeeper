package com.example.mynotekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.mynotekeeper.MyNoteContentProviderContract.Courses;

import java.util.List;

import static com.example.mynotekeeper.MyNoteDbContract.CourseEntryInfo;
import static com.example.mynotekeeper.MyNoteDbContract.NoteInfoEntry;

public class NoteActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinner;
    private EditText mTitleText;
    private EditText mBodyText;
    private int mNoteId;
    private boolean mIsCancelling;
    private NoteActivityViewModel mViewModel;
    private MyNoteOpenHelper mMyNoteOpenHelper;
    private SimpleCursorAdapter mCourseAdapter;
    private Cursor mNoteCursor;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mNoteTextPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(mViewModel.mViewIsNew && savedInstanceState != null)
            mViewModel.restoreState(savedInstanceState);

        mViewModel.mViewIsNew = false;

        mMyNoteOpenHelper = new MyNoteOpenHelper(this);

        mSpinner = findViewById(R.id.spinner_courses);
        mTitleText = findViewById(R.id.text_title);
        mBodyText = findViewById(R.id.text_body);


        mCourseAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null,
                new String[]{CourseEntryInfo.COLUMN_COURSE_TITLE},
                new int[]{android.R.id.text1},0);
        mCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mCourseAdapter);

        //getLoaderManager().initLoader(Constants.COURSE_LOADER, null, this)

       // loadCourseData();


        readCurrentNoteState();
        saveOriginalNote();

        if(!mIsNewNote)
        loadNoteData();
    }

    private void loadNoteData() {
        SQLiteDatabase db = mMyNoteOpenHelper.getReadableDatabase();

        String selection = NoteInfoEntry._ID + " = ? ";

        String[] selectionArg = {Integer.toString(mNoteId)};

        String[] columns = {
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE
        };

        mNoteCursor = db.query(NoteInfoEntry.TABLE_NAME,columns,
                selection ,selectionArg,
                null, null, null);

        mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE);

        mNoteCursor.moveToNext();

        displayNotes();
    }

    private void loadCourseData() {
        SQLiteDatabase db = mMyNoteOpenHelper.getReadableDatabase();
        String[] courseColumns = {CourseEntryInfo.COLUMN_COURSE_ID, CourseEntryInfo.COLUMN_COURSE_TITLE};
        String courseOrder = CourseEntryInfo.COLUMN_COURSE_TITLE;

        Cursor courseCursor = db.query(CourseEntryInfo.TABLE_NAME, courseColumns, null, null, null, null, courseOrder);
        mCourseAdapter.changeCursor(courseCursor);

    }

    @Override
    protected void onDestroy() {
        mMyNoteOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.send_item){
            sendEmail();
        }
        if(item.getItemId() == R.id.cancel_item){
            mIsCancelling = true;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveOriginalNote() {
        if(mIsNewNote)
            return;
        mViewModel.mOriginalCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalTitle = mTitleText.getText().toString();
        mViewModel.mOriginalBody = mBodyText.getText().toString();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(outState != null)
            mViewModel.saveState(outState);
    }


    private void restorePreviouslyNote() {

        CourseInfo courseInfo = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId);
        mNote.setCourse(courseInfo);
        mNote.setTitle(mViewModel.mOriginalTitle);
        mNote.setText(mViewModel.mOriginalBody);
    }

    public void displayNotes(){
        String courseId = mNoteCursor.getString(mCourseIdPos);
        String noteTitle = mNoteCursor.getString(mNoteTitlePos);
        String noteText = mNoteCursor.getString(mNoteTextPos);
        List<CourseInfo> courseInfos = DataManager.getInstance().getCourses();
        CourseInfo course = DataManager.getInstance().getCourse(courseId);
        int courseIndex = courseInfos.indexOf(course);
        mSpinner.setSelection(courseIndex);
        mTitleText.setText(noteTitle);
        mBodyText.setText(noteText);

    }

    public void readCurrentNoteState(){
        Intent intent = getIntent();
       // mNote = intent.getParcelableExtra(Constants.NOTE_INFO);
        mNoteId = intent.getIntExtra(Constants.NOTE_INFO, Constants.ID_NOT_SET);

        mIsNewNote = mNoteId == Constants.ID_NOT_SET;
        if(mIsNewNote){
            createNewNote();
        }
            mNote = DataManager.getInstance().getNotes().get(mNoteId);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNoteId = dm.createNewNote();
        //mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            if(mIsNewNote){
                DataManager.getInstance().removeNote(mNoteId);
            }else {
                restorePreviouslyNote();
            }

        }else {
            saveNote();
        }

    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinner.getSelectedItem());
        mNote.setTitle(mTitleText.getText().toString());
        mNote.setText(mBodyText.getText().toString());
    }

    public void sendEmail(){
        CourseInfo course = (CourseInfo) mSpinner.getSelectedItem();
        String subject = mTitleText.getText().toString();
        String body = "im learning something new here at plural site \"" + course.getTitle() + "\"\n"  + mBodyText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(intent);

    }





    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = null;

        if(id == Constants.COURSE_LOADER){
            loader = createLoaderCourses();
        }
        return loader;
    }

    private CursorLoader createLoaderCourses(){

        Uri uri = Courses.CONTENT_URI;
        String[] courseColumns = {  Courses.COLUMN_COURSE_ID,
                                    Courses.COLUMN_COURSE_TITLE,
                                    Courses._ID };

        return new CursorLoader(this, uri, courseColumns, null, null, Courses.COLUMN_COURSE_TITLE);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
