package com.shleen.quixotic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDialog extends DialogFragment {

    private Note note;

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();

        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_note_dialog, null, false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        // Set typeface for TextViews
        Typeface BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");

        TextView txt_note_date = (TextView) v.findViewById(R.id.txt_note_date);
        TextView txt_note = (TextView) v.findViewById(R.id.txt_note);
        TextView txt_attribution = (TextView) v.findViewById(R.id.txt_attribution);

        txt_note_date.setTypeface(BUTLER_REG);
        txt_note.setTypeface(BUTLER_REG);
        txt_attribution.setTypeface(BUTLER_REG);

        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy 'at' hh:mm a", Locale.ENGLISH);
        txt_note_date.setText(df.format(new Date(Long.parseLong(note.getCreated_on()) * 1000)));
        txt_note.setText(note.getNote());

        String attribution = "";
        if (note.getAuthor().trim().length() > 0)
        {
            if (note.getWork().trim().length() > 0) { attribution = String.format("By %s, \nin %s", note.getAuthor(), note.getWork()); }
            else { attribution = String.format("By %s", note.getAuthor()); }
        }
        else if (note.getWork().trim().length() > 0) {
            attribution = String.format("in %s", note.getWork());
        }
        else { txt_attribution.setVisibility(View.GONE); }
        txt_attribution.setText(attribution);

        builder.setView(v);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    void showNoteDialog(@NonNull FragmentManager manager, Note note) {
        super.show(manager, "noteDialog");
        this.note = note;
    }
}
