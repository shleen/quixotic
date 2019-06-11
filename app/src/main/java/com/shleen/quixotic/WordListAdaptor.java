package com.shleen.quixotic;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WordListAdaptor extends RecyclerView.Adapter<WordListAdaptor.ViewHolder> {

    private List<Word> words;
    private static onWordClickListener listener;

    private Typeface BUTLER_REG;
    private Typeface BUTLER_MED;

    public WordListAdaptor(List<Word> words, onWordClickListener listener) {
        this.words = words;
        this.listener = listener;
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
        holder.txt_definition.setText(w.getDefinitions().get(0).getDefinition());
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> w) {
        this.words = w;
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txt_word, txt_definition;

        public ViewHolder(View itemView) {
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
