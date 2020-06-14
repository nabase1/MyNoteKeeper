package com.example.mynotekeeper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    static DataManager sDataManager;

    @Before
    public void setUp(){
        sDataManager = DataManager.getInstance();
        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();

    }

    @Test
    public void createNewNote() {
        sDataManager = DataManager.getInstance();
        final CourseInfo courseInfo = sDataManager.getCourse("android_async");
        final String textTitle = "Title of Note";
        final String textBody = "This is the body Test";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo noteNew = sDataManager.getNotes().get(noteIndex);
        noteNew.setCourse(courseInfo);
        noteNew.setTitle(textTitle);
        noteNew.setText(textBody);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(compareNote.getCourse(), courseInfo);
        assertEquals(compareNote.getTitle(), textTitle);
        assertEquals(compareNote.getText(), textBody);
    }

    @Test
    public void compareNotes(){
        sDataManager = DataManager.getInstance();
        final CourseInfo courseInfo = sDataManager.getCourse("android_async");
        final String textTitle = "Title of Note1";
        final String textBody = "This is the body Test";
        final String textBody2 = "This is the second body Test 2";

        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo noteNew1 = sDataManager.getNotes().get(noteIndex1);
        noteNew1.setCourse(courseInfo);
        noteNew1.setTitle(textTitle);
        noteNew1.setText(textBody);

        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo noteNew2 = sDataManager.getNotes().get(noteIndex2);
        noteNew2.setCourse(courseInfo);
        noteNew2.setTitle(textTitle);
        noteNew2.setText(textBody2);

        int foundNote1 = sDataManager.findNote(noteNew1);
        assertEquals(noteIndex1, foundNote1);

        int foundNote2 = sDataManager.findNote(noteNew2);
        assertEquals(noteIndex2, foundNote2);
    }

}