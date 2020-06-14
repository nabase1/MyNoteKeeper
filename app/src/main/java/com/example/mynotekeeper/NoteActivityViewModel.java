package com.example.mynotekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel extends ViewModel {
    public String mOriginalCourseId;
    public String mOriginalTitle;
    public String mOriginalBody;
    public boolean mViewIsNew = true;

    public void saveState(Bundle outState) {
        outState.putString(Constants.COURSE_ID, mOriginalCourseId);
        outState.putString(Constants.NOTE_TITLE, mOriginalTitle);
        outState.putString(Constants.NOTE_BODY, mOriginalBody);
    }

    public void restoreState(Bundle inState){
        mOriginalCourseId = inState.getString(Constants.COURSE_ID);
        mOriginalTitle = inState.getString(Constants.NOTE_TITLE);
        mOriginalBody = inState.getString(Constants.NOTE_BODY);
    }
}
