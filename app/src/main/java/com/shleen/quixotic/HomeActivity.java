package com.shleen.quixotic;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WordListAdaptor wordListAdaptor;

    private FloatingActionButton toggle_sort;
    boolean sort_by_created = true;

    List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        wordListAdaptor = WordDataHolder.getInstance().getWordListAdaptor();
        words = wordListAdaptor.getWords();

        recyclerView = (RecyclerView) findViewById(R.id.word_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wordListAdaptor);

        toggle_sort = (FloatingActionButton) findViewById(R.id.toggle_sort);
        toggle_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort_by_created) {
                    // Sort by recently added
                    Collections.sort(words, new AddedSorter());
                    sort_by_created = false;
                } else {
                    // Sort alphabetically
                    Collections.sort(words, new WordSorter());
                    sort_by_created = true;
                }
                wordListAdaptor.setWords(words);
                wordListAdaptor.notifyDataSetChanged();
            }
        });
    }

}
