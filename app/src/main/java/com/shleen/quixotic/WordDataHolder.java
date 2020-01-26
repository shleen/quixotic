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

public class WordDataHolder {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    private List<Word> words = new ArrayList<>();

    private WordListAdaptor wordListAdaptor;

    GoogleSignInAccount user;

    WordDataHolder(Context c) {

        user = GoogleSignIn.getLastSignedInAccount(c);
        ref =  database.getReference(String.format("%s/words/", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

        wordListAdaptor = new WordListAdaptor(words, new onWordClickListener() {
            @Override
            public void onWordClicked(View v, int position) {

                // Navigate to word-specific page
                Intent i = new Intent(v.getContext(), WordActivity.class);
                i.putExtra("WORD", words.get(position));

                v.getContext().startActivity(i);

            }
        });
    }

    public List<Word> getData() { return words; }
    public void setData() {

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

                    // Create word
                    if (childDataSnapshot.child("word").getValue() != null) {
                        Word word = new Word(childDataSnapshot.child("word").getValue().toString(),
                                childDataSnapshot.child("phonetic").getValue().toString(),
                                definitions,
                                childDataSnapshot.child("created_on").getValue().toString());

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

    public WordListAdaptor getWordListAdaptor() { return wordListAdaptor; }

    private static WordDataHolder holder = null;
    public static WordDataHolder getInstance() { return holder; }
    public static void setInstance(WordDataHolder h) { holder = h; }
}
