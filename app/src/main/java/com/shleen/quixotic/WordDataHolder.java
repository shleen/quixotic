package com.shleen.quixotic;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class WordDataHolder {

    private DatabaseReference ref;

    private List<Word> words = new ArrayList<>();

    private WordListAdaptor wordListAdaptor;

    private GoogleSignInAccount user;

    WordDataHolder(final Context c) {

        user = GoogleSignIn.getLastSignedInAccount(c);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref =  database.getReference(String.format("%s/words/", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

        wordListAdaptor = new WordListAdaptor(words, new onWordClickListener() {
            @Override
            public void onWordClicked(View v, int position) {
                // Navigate to word-specific page
                ((BaseActivity) c).goToWord(words.get(position));
            }
        }, c);
    }

    List<Word> getData() { return words; }
    void setData() {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                words.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    // Create array of definitions
                    ArrayList<Definition> definitions = new ArrayList<>();
                    for (DataSnapshot child : childDataSnapshot.child("definitions").getChildren()) {
                        definitions.add(child.getValue(Definition.class));
                    }

                    // Create array of examples
                    ArrayList<String> examples = new ArrayList<>();
                    for (DataSnapshot child : childDataSnapshot.child("examples").getChildren()) {
                        examples.add(child.getValue().toString());
                    }

                    // Create word
                    if (childDataSnapshot.child("word").getValue() != null) {
                        Word word = new Word(childDataSnapshot.child("word").getValue().toString(),
                                childDataSnapshot.child("phonetic").getValue().toString(),
                                definitions,
                                childDataSnapshot.child("createdOn").getValue().toString(),
                                examples);

                        words.add(word);
                    }
                }

                // Sort words
                Collections.sort(words, new AddedSorter());

                // Update recycler view
                wordListAdaptor.notifyDataSetChanged();
                wordListAdaptor.getSections();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO: Handle database error
            }
        });
    }

    WordListAdaptor getWordListAdaptor() { return wordListAdaptor; }

    private static WordDataHolder holder = null;
    static WordDataHolder getInstance() { return holder; }
    static void setInstance(WordDataHolder h) { holder = h; }
}
