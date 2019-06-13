package com.shleen.quixotic;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.Collections;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static com.shleen.quixotic.MainActivity.sort_alphabetically;

public class HomeActivity extends AppCompatActivity {

    private IndexFastScrollRecyclerView recyclerView;
    private WordListAdaptor wordListAdaptor;

    private FloatingActionButton toggle_sort;

    List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        wordListAdaptor = WordDataHolder.getInstance().getWordListAdaptor();
        words = wordListAdaptor.getWords();
        Collections.sort(words, new AddedSorter());
        wordListAdaptor.setWords(words);

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
                // Sort words
                words = new Util().sortWords(words);

                // Update recycler view
                wordListAdaptor.setWords(words);
                wordListAdaptor.notifyDataSetChanged();
                wordListAdaptor.getSections();

                // Toggle visibility of the index bar
                recyclerView.setIndexBarVisibility(sort_alphabetically);
            }
        });
    }

}
