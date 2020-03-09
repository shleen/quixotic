package com.shleen.quixotic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class NoResultDialog extends DialogFragment {

    String word;

    private Typeface BUTLER_REG;

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

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_no_result_dialog, null, false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setView(v);

        // Set typeface for TextViews
        BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");

        // Set title
        txt_title = (TextView) v.findViewById(R.id.title);
        txt_title.setTypeface(BUTLER_REG);
        txt_title.setText(String.format("Could not add %s.", word));

        // Set message
        txt_message = (TextView) v.findViewById(R.id.message);
        txt_message.setTypeface(BUTLER_REG);
        txt_message.setText(String.format("No definitions found for %s.", word));

        // Set btn_ok
        btn_ok = (Button) v.findViewById(R.id.btn_ok);
        btn_ok.setTypeface(BUTLER_REG);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss dialog
                dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void showNoResultDialog(FragmentManager manager, String tag, String w) {
        word = w;

        show(manager, tag);
    }
}
