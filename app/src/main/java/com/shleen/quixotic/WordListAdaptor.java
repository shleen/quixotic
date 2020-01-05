package com.shleen.quixotic;

import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.firebase.functions.FirebaseFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordListAdaptor extends RecyclerView.Adapter<WordListAdaptor.ViewHolder> implements SectionIndexer {

    private List<Word> words;
    private ArrayList<Integer> mSectionPositions;
    private static onWordClickListener listener;

    private Typeface BUTLER_REG;
    private Typeface BUTLER_MED;

    private FirebaseFunctions mFunctions;

    WordListAdaptor(List<Word> words, onWordClickListener listener) {
        this.words = words;
        this.listener = listener;

        // Initialize Firebase functions instance
        mFunctions = FirebaseFunctions.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word w = words.get(position);

        holder.txt_word.setTypeface(BUTLER_MED);
        holder.txt_word.setText(w.getWord());

        holder.txt_definition.setTypeface(BUTLER_REG);

        if (w.getDefinitions().size() > 0)
        {
            holder.txt_definition.setText(w.getDefinitions().get(0).getDefinition());
        }
    }

    List<Word> getWords() {
        return words;
    }

    void setWords(List<Word> w) {
        this.words = w;
    }

    void removeWordAt(int position) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("word", words.get(position).getWord());

        // Execute call
        mFunctions.getHttpsCallable("deleteWord").call(data);

        // Remove from words list
        words.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = words.size(); i < size; i++) {
            String section = String.valueOf(words.get(i).getWord().charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txt_word, txt_definition;

        ViewHolder(View itemView) {
            super(itemView);

            BUTLER_REG = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Butler_Regular.ttf");
            BUTLER_MED = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Butler_Medium.ttf");

            txt_word = (TextView) itemView.findViewById(R.id.list_word);
            txt_definition = (TextView) itemView.findViewById(R.id.list_definiton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onWordClicked(v, getLayoutPosition());
        }
    }
}
