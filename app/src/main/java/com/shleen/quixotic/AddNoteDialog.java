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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AddNoteDialog extends DialogFragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    private TextInputEditText edt_note;
    private TextInputEditText edt_author;
    private TextInputEditText edt_work;

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        Point size = new Point();

        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.8), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MaterialButton btn_add;

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_add_note_dialog, null, false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        // Get signed-in user
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(v.getContext());

        // Get reference to user's words
        ref = database.getReference(String.format("%s/notes", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

        // Set typeface for TextViews
        Typeface BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");

        TextView txt_note = (TextView) v.findViewById(R.id.txt_note);
        TextView txt_author = (TextView) v.findViewById(R.id.txt_author);
        TextView txt_work = (TextView) v.findViewById(R.id.txt_work);

        txt_note.setTypeface(BUTLER_REG);
        txt_author.setTypeface(BUTLER_REG);
        txt_work.setTypeface(BUTLER_REG);

        edt_note = (TextInputEditText) v.findViewById(R.id.edt_note);
        edt_author = (TextInputEditText) v.findViewById(R.id.edt_author);
        edt_work = (TextInputEditText) v.findViewById(R.id.edt_work);

        edt_note.setTypeface(BUTLER_REG);
        edt_author.setTypeface(BUTLER_REG);
        edt_work.setTypeface(BUTLER_REG);

        // Set btn_add functionality
        btn_add = (MaterialButton) v.findViewById(R.id.btn_add_note);
        btn_add.setTypeface(BUTLER_REG);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate
                if (edt_note.getText().toString().trim().length() > 0)
                {
                    // Push note to Firebase
                    Map<String, String> note_info = new HashMap<>();
                    note_info.put("note", edt_note.getText().toString());
                    note_info.put("author", edt_author.getText().toString());
                    note_info.put("work", edt_work.getText().toString());
                    note_info.put("createdOn", Long.toString(Instant.now().getEpochSecond()));

                    ref.push().setValue(note_info);

                    dismiss();
                }
                else
                {
                    // TODO: Handle no valid note written
                    Toast.makeText(getContext(), "No note entered!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setView(v);

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
