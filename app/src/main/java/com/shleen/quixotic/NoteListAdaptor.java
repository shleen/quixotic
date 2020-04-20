package com.shleen.quixotic;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteListAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> notes;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView card_note;
        ViewHolder(CardView v) {
            super(v);
            card_note = v;
        }
    }

    public static class AddNoteViewHolder extends RecyclerView.ViewHolder {

        AddNoteViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    NoteListAdaptor(List<Note> notes) { this.notes = notes; }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_note_card,parent,false);
            return new AddNoteViewHolder(view);
        }
        else {
            // create a new view
            CardView v = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note, parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            AddNoteViewHolder h = (AddNoteViewHolder) holder;

        }
        else {
            ViewHolder h = (ViewHolder) holder;
            ((TextView) h.card_note.findViewById(R.id.txt_note)).setText(notes.get(position).getNote());
        }

    }

    // Returns the list of notes
    List<Note> getNotes() { return notes; }

    // Sets the list of notes
    void setNotes(List<Note> notes) { this.notes = notes; }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
