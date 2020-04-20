package com.shleen.quixotic;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class NoteDataHolder {

    private DatabaseReference ref;

    private List<Note> notes = new ArrayList<>();

    private NoteListAdaptor noteListAdaptor;

    NoteDataHolder(Context c) {

        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(c);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref =  database.getReference(String.format("%s/notes/", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

        noteListAdaptor = new NoteListAdaptor(notes);
    }

    List<Note> getData() { return notes; }
    void setData() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notes.clear();
                notes.add(null);

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    // Create note
                    Note note = new Note(childDataSnapshot.child("note").getValue().toString(),
                                         childDataSnapshot.child("author").getValue().toString(),
                                         childDataSnapshot.child("work").getValue().toString(),
                                         childDataSnapshot.child("createdOn").getValue().toString());

                    notes.add(note);
                }

                // Update recycler view
                noteListAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO: Handle database error
            }
        });
    }

    NoteListAdaptor getNoteListAdaptor() { return noteListAdaptor; }

    private static NoteDataHolder holder = null;
    static NoteDataHolder getInstance() { return holder; }
    static void setInstance(NoteDataHolder h) { holder = h; }
}
