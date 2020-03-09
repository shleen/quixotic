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

public class DeleteDialog extends DialogFragment {

    WordListAdaptor wordListAdaptor;
    int position;

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

        Button btn_no;
        Button btn_yes;

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_delete_dialog, null, false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setView(v);

        // Set typeface for TextViews
        BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");

        // Set title
        txt_title = (TextView) v.findViewById(R.id.title);
        txt_title.setTypeface(BUTLER_REG);
        txt_title.setText(String.format("Delete %s?", wordListAdaptor.getWords().get(position).getWord()));

        // Set message
        txt_message = (TextView) v.findViewById(R.id.message);
        txt_message.setTypeface(BUTLER_REG);
        txt_message.setText(String.format("Are you sure you want to delete %s?", wordListAdaptor.getWords().get(position).getWord()));

        // Set btn_no
        btn_no = (Button) v.findViewById(R.id.btn_no);
        btn_no.setTypeface(BUTLER_REG);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss dialog
                dismiss();
            }
        });

        // Set btn_yes
        btn_yes = (Button) v.findViewById(R.id.btn_yes);
        btn_yes.setTypeface(BUTLER_REG);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss dialog
                dismiss();

                // Remove word
                wordListAdaptor.removeWordAt(position);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void showDeleteDialog(FragmentManager manager, String tag, WordListAdaptor word_list_adaptor, int pos, SwipeToDeleteCallback callback) {
        wordListAdaptor = word_list_adaptor;
        position = pos;

        show(manager, tag);
    }

    @Override
    public void dismiss() {
        super.dismiss();

        // Reset swipe view
        wordListAdaptor.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        // Reset swipe view
        wordListAdaptor.notifyDataSetChanged();
    }
}
