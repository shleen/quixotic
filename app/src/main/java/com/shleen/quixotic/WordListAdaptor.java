package com.shleen.quixotic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WordListAdaptor extends RecyclerView.Adapter<WordListAdaptor.ViewHolder> {
    private List<Word> words;
    private static onWordClickListener listener;

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
        holder.txtword.setText(w.getWord());
        holder.txtdefinition.setText(w.getDefinitions().get(0).getDefinition());
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtword, txtdefinition;

        public ViewHolder(View itemView) {
            super(itemView);

            txtword = (TextView) itemView.findViewById(R.id.list_word);
            txtdefinition = (TextView) itemView.findViewById(R.id.list_definiton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onWordClicked(v, getLayoutPosition());

        }
    }
}
