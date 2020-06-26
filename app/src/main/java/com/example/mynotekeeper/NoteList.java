package com.example.mynotekeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

public class NoteList extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mNoteAdapter;
    private NoteAdapter mNoteAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initializeListContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNoteAdapter1.notifyDataSetChanged();
    }

    private void initializeListContent() {

        final RecyclerView recyclerView = findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(notesLayoutManager);

        List<NoteInfo> noteInfo = DataManager.getInstance().getNotes();
        mNoteAdapter1 = new NoteAdapter(this, noteInfo);
        recyclerView.setAdapter(mNoteAdapter1);
    }
}