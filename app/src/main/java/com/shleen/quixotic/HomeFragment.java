package com.shleen.quixotic;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.Collections;
import java.util.List;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

import static com.shleen.quixotic.MainFragment.sort_alphabetically;

public class HomeFragment extends Fragment {

    private IndexFastScrollRecyclerView recyclerView;
    private WordListAdaptor wordListAdaptor;

    List<Word> words;

    MaterialButtonToggleGroup toggle_sort;
    MaterialButton btn_sort_chrono;

    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_home, container, false);

        wordListAdaptor = WordDataHolder.getInstance().getWordListAdaptor();
        words = wordListAdaptor.getWords();
        Collections.sort(words, new AddedSorter());
        wordListAdaptor.setWords(words);

        recyclerView = (IndexFastScrollRecyclerView) view.findViewById(R.id.word_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setIndexBarVisibility(false);
        recyclerView.setIndexBarTransparentValue((float) 0);
        recyclerView.setIndexBarTextColor(R.color.colorLighter);
        recyclerView.setAdapter(wordListAdaptor);

        // Set up top bar
        btn_sort_chrono = (MaterialButton) view.findViewById(R.id.btn_sort_chrono);
        btn_sort_chrono.setChecked(true);

        toggle_sort = (MaterialButtonToggleGroup) view.findViewById(R.id.toggle_sort);
        toggle_sort.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                if (isChecked) {
                    // Sort words
                    words = new Util().sortWords(words);

                    // Update recycler view
                    wordListAdaptor.setWords(words);
                    wordListAdaptor.notifyDataSetChanged();
                    wordListAdaptor.getSections();

                    // Toggle visibility of the index bar
                    recyclerView.setIndexBarVisibility(sort_alphabetically);
                }

            }
        });

        // Initialize swipe to delete
        enableSwipeToDelete();

        return view;
    }

    private void enableSwipeToDelete() {
        final SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(view.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                // Display delete dialog + handle user input
                DeleteDialog deleteDialog = new DeleteDialog();
                deleteDialog.showDeleteDialog(getFragmentManager(), "deleteDialog", wordListAdaptor, viewHolder.getAdapterPosition(), this);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
