package com.shleen.quixotic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("words");

    private RecyclerView recyclerView;
    private WordListAdaptor wordListAdaptor;

    private List<Word> words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.word_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    // Create array of definitions
                    ArrayList<Definition> definitions = new ArrayList<>();
                    for (DataSnapshot child : childDataSnapshot.child("definitions").getChildren()) {

                        definitions.add(child.getValue(Definition.class));

                    }

                    // Create word
                    Word word = new Word(childDataSnapshot.child("word").getValue().toString(),
                            childDataSnapshot.child("phonetic").getValue().toString(),
                            definitions);

                    words.add(word);
                }

                wordListAdaptor = new WordListAdaptor(words, new onWordClickListener() {
                    @Override
                    public void onWordClicked(View v, int position) {

                        // Navigate to word-specific page
                        Intent i = new Intent(v.getContext(), WordActivity.class);
                        i.putExtra("WORD", words.get(position));

                        startActivity(i);

                    }
                });
                recyclerView.setAdapter(wordListAdaptor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO: Handle database error
            }
        });
    }
}
