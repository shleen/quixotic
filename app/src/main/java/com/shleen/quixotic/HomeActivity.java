package com.shleen.quixotic;

import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import java.util.Collections;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static com.shleen.quixotic.MainActivity.sort_alphabetically;

public class HomeActivity extends BaseActivity {

    private IndexFastScrollRecyclerView recyclerView;
    private WordListAdaptor wordListAdaptor;

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

        FloatingActionButton toggle_sort = (FloatingActionButton) findViewById(R.id.toggle_sort);
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

        // Initialize swipe to delete
        enableSwipeToDelete();
    }

    private void enableSwipeToDelete() {
        final SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                // Display delete dialog + handle user input
                DeleteDialog deleteDialog = new DeleteDialog();
                deleteDialog.showDeleteDialog(getSupportFragmentManager(), "deleteDialog", wordListAdaptor, viewHolder.getAdapterPosition(), this);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
