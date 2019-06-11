package com.shleen.quixotic;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.Collections;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

public class HomeActivity extends AppCompatActivity {

    private IndexFastScrollRecyclerView recyclerView;
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
        Collections.sort(words, new AddedSorter());

        recyclerView = (IndexFastScrollRecyclerView) findViewById(R.id.word_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setIndexBarVisibility(false);
        recyclerView.setIndexBarTransparentValue((float) 0);
        recyclerView.setIndexBarTextColor(R.color.colorLighter);
        recyclerView.setAdapter(wordListAdaptor);

        toggle_sort = (FloatingActionButton) findViewById(R.id.toggle_sort);
        toggle_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sort_by_created) {
                    // Sort by recently added
                    Collections.sort(words, new WordSorter());
                    sort_by_created = false;
                } else {
                    // Sort alphabetically
                    Collections.sort(words, new AddedSorter());
                    sort_by_created = true;
                }
                wordListAdaptor.setWords(words);
                wordListAdaptor.notifyDataSetChanged();
                wordListAdaptor.getSections();

                recyclerView.setIndexBarVisibility(!sort_by_created);
            }
        });
    }

}
