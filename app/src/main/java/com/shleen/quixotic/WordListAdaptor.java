package com.shleen.quixotic;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WordListAdaptor extends RecyclerView.Adapter<WordListAdaptor.ViewHolder> {
    private List<Word> words;

    public WordListAdaptor(List<Word> words) {
        this.words = words;
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtword, txtdefinition;
        public ViewHolder(View itemView) {
            super(itemView);
            txtword=(TextView)itemView.findViewById(R.id.list_word);
            txtdefinition=(TextView)itemView.findViewById(R.id.list_definiton);
        }
    }
}
