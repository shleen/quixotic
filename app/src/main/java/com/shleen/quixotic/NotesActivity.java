package com.shleen.quixotic;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesActivity extends BaseActivity {

    List<Note> notes;

    RecyclerView rcl_notes;
    NoteListAdaptor noteListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Set up recyclerview
        rcl_notes = findViewById(R.id.rcl_notes);

        rcl_notes.setHasFixedSize(true);

        // specify an adapter
        noteListAdaptor = NoteDataHolder.getInstance().getNoteListAdaptor();
        notes = noteListAdaptor.getNotes();
        noteListAdaptor.setNotes(notes);

        rcl_notes.setAdapter(noteListAdaptor);

        // Set nav listeners
        setNavListeners();
    }

    public void openAddNoteDialog(View v) {
        AddNoteDialog addNoteDialog = new AddNoteDialog();
        addNoteDialog.show(getSupportFragmentManager(), "addNoteDialog");
    }

}
