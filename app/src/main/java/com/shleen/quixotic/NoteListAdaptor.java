package com.shleen.quixotic;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteListAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> notes;

    private Typeface BUTLER_REG;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_note;
        ViewHolder(CardView v) {
            super(v);
            card_note = v;
        }
    }

    public static class AddNoteViewHolder extends RecyclerView.ViewHolder {

        CardView card_add;

        AddNoteViewHolder(@NonNull CardView itemView) {
            super(itemView);

            card_add = itemView;
        }
    }

    NoteListAdaptor(List<Note> notes) { this.notes = notes; }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.add_note_card,parent,false);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == 0) {
            AddNoteViewHolder h = (AddNoteViewHolder) holder;

            // Set typeface & font size of textviews
            BUTLER_REG = Typeface.createFromAsset(h.itemView.getContext().getAssets(), "fonts/Butler_Regular.ttf");
            Typeface BUTLER_MED = Typeface.createFromAsset(h.itemView.getContext().getAssets(), "fonts/Butler_Medium.ttf");

            ((TextView) h.card_add.findViewById(R.id.txt_add_note_title)).setTypeface(BUTLER_MED);
            ((TextView) h.card_add.findViewById(R.id.txt_add_note_subtitle)).setTypeface(BUTLER_REG);
        }
        else {
            ViewHolder h = (ViewHolder) holder;

            TextView txt_note_date = (TextView) h.card_note.findViewById(R.id.txt_note_date);
            txt_note_date.setTypeface(BUTLER_REG);

            DateFormat df = new SimpleDateFormat("E, dd MMM yyyy 'at' hh:mm a", Locale.ENGLISH);
            txt_note_date.setText(df.format(new Date(Long.parseLong(notes.get(position).getCreated_on()) * 1000)));

            TextView txt_note = (TextView) h.card_note.findViewById(R.id.txt_note);
            txt_note.setTypeface(BUTLER_REG);
            txt_note.setText(notes.get(position).getNote());

            h.card_note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoteDialog noteDialog = new NoteDialog();
                    noteDialog.showNoteDialog(((FragmentActivity) v.getContext()).getSupportFragmentManager(), notes.get(position));
                }
            });
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
