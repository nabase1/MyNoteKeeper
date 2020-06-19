package com.example.mynotekeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.zip.Inflater;

public class NoteActivity extends AppCompatActivity {

    private NoteInfo mNote;
    private boolean mIsNewNote;
    private Spinner mSpinner;
    private EditText mTitleText;
    private EditText mBodyText;
    private int mNotePosition;
    private boolean mIsCancelling;
    private NoteActivityViewModel mViewModel;

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

        mSpinner = findViewById(R.id.spinner_courses);
        mTitleText = findViewById(R.id.text_title);
        mBodyText = findViewById(R.id.text_body);

        List<CourseInfo> courseInfo = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseInfo);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(courseAdapter);



        readCurrentNoteState();
        saveOriginalNote();

        if(!mIsNewNote)
        displayNotes(mSpinner, mTitleText, mBodyText);
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

    public void displayNotes(Spinner spinner, EditText titleText, EditText bodyText){
        List<CourseInfo> courseInfos = DataManager.getInstance().getCourses();
        int courseIndex = courseInfos.indexOf(mNote.getCourse());
        spinner.setSelection(courseIndex);
        titleText.setText(mNote.getTitle());
        bodyText.setText(mNote.getText());

    }

    public void readCurrentNoteState(){
        Intent intent = getIntent();
       // mNote = intent.getParcelableExtra(Constants.NOTE_INFO);
        mNotePosition = intent.getIntExtra(Constants.NOTE_INFO, Constants.POSITION_NOT_SET);

        mIsNewNote = mNotePosition == Constants.POSITION_NOT_SET;
        if(mIsNewNote){
            createNewNote();
        }
            mNote = DataManager.getInstance().getNotes().get(mNotePosition);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        //mNote = dm.getNotes().get(mNotePosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
            if(mIsNewNote){
                DataManager.getInstance().removeNote(mNotePosition);
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
}
