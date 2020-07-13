package com.shleen.quixotic;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesFragment extends Fragment {

    List<Note> notes;

    RecyclerView rcl_notes;
    NoteListAdaptor noteListAdaptor;


    View view;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Set up recyclerview
        rcl_notes = view.findViewById(R.id.rcl_notes);

        rcl_notes.setHasFixedSize(true);

        // specify an adapter
        noteListAdaptor = NoteDataHolder.getInstance().getNoteListAdaptor();
        notes = noteListAdaptor.getNotes();
        noteListAdaptor.setNotes(notes);

        rcl_notes.setAdapter(noteListAdaptor);

        return view;
    }

    public void openAddNoteDialog(View v) {
        AddNoteDialog addNoteDialog = new AddNoteDialog();
        addNoteDialog.show(getFragmentManager(), "addNoteDialog");
    }

}
