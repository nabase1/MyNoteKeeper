package com.example.mynotekeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

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

        mSpinner = findViewById(R.id.spinner_courses);
        mTitleText = findViewById(R.id.text_title);
        mBodyText = findViewById(R.id.text_body);

        List<CourseInfo> courseInfo = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> courseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseInfo);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(courseAdapter);

        readCurrentNoteState();
        saveOriginalNote();
    }

    private void saveOriginalNote() {
        if(mIsNewNote)
            return;
        mViewModel.mOriginalCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalTitle = mTitleText.getText().toString();
        mViewModel.mOriginalBody = mBodyText.getText().toString();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        restorePreviouslyNote();
    }

    private void restorePreviouslyNote() {

        CourseInfo courseInfo = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId);
        mNote.setCourse(courseInfo);
        mNote.setTitle(mViewModel.mOriginalTitle);
        mNote.setText(mViewModel.mOriginalBody);
    }

    public void displayNotes(){
        List<CourseInfo> courseInfos = DataManager.getInstance().getCourses();
        int courseIndex = courseInfos.indexOf(mNote.getCourse());
        mSpinner.setSelection(courseIndex);
        mTitleText.setText(mNote.getTitle());
        mBodyText.setText(mNote.getText());

    }

    public void readCurrentNoteState(){
        Intent intent = getIntent();
       // mNote = intent.getParcelableExtra(Constants.NOTE_INFO);
        int position = intent.getIntExtra(Constants.NOTE_INFO, Constants.POSITION_NOT_SET);

        mIsNewNote = mNote == null;
        if(mIsNewNote){
            createNewNote();
        }else {
            mNote = DataManager.getInstance().getNotes().get(position);
        }

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);
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
