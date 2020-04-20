package com.shleen.quixotic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AddNoteDialog extends DialogFragment {

    String word;

    private Typeface BUTLER_REG;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    EditText edt_note;
    EditText edt_author;
    EditText edt_work;

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

        TextView txt_title;
        TextView txt_message;

        Button btn_ok;
        Button btn_add;

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_add_note_dialog, null, false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setView(v);

        // Get signed-in user
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(v.getContext());

        // Get reference to user's words
        ref = database.getReference(String.format("%s/notes", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

//        // Set typeface for TextViews
//        BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");
//
//        // Set title
//        txt_title = (TextView) v.findViewById(R.id.title);
//        txt_title.setTypeface(BUTLER_REG);
//        txt_title.setText(String.format("Could not add %s.", word));
//
//        // Set message
//        txt_message = (TextView) v.findViewById(R.id.message);
//        txt_message.setTypeface(BUTLER_REG);
//        txt_message.setText(String.format("No definitions found for %s.", word));
//
//        // Set btn_ok
//        btn_ok = (Button) v.findViewById(R.id.btn_ok);
//        btn_ok.setTypeface(BUTLER_REG);
//        btn_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Dismiss dialog
//                dismiss();
//            }
//        });

        edt_note = (EditText) v.findViewById(R.id.edt_note);
        edt_author = (EditText) v.findViewById(R.id.edt_author);
        edt_work = (EditText) v.findViewById(R.id.edt_work);

        // Set btn_add functionality
        btn_add = (Button) v.findViewById(R.id.btn_add_note);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Push note to Firebase
                Map<String, String> note_info = new HashMap<>();
                note_info.put("note", edt_note.getText().toString());
                note_info.put("author", edt_author.getText().toString());
                note_info.put("work", edt_work.getText().toString());
                note_info.put("createdOn", Long.toString(Instant.now().getEpochSecond()));

                ref.push().setValue(note_info);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
