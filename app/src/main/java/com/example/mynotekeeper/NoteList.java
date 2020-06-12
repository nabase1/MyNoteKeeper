package com.example.mynotekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteList extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> mNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        initializeListContent();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mNoteAdapter.notifyDataSetChanged();
    }

    private void initializeListContent() {

        ListView listView = findViewById(R.id.listNote);

        final List<NoteInfo> noteList = DataManager.getInstance().getNotes();
        mNoteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteList);

        listView.setAdapter(mNoteAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteList.this, NoteActivity.class);
                //NoteInfo note = (NoteInfo) noteList.get(position);
                intent.putExtra(Constants.NOTE_INFO, position);
                startActivity(intent);
            }
        });
    }
}