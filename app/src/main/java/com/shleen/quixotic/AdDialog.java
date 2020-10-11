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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdDialog extends DialogFragment {

    private Typeface BUTLER_REG;
    private Typeface BUTLER_BOLD;

    Button btn_yes;

    InterstitialAd ad;

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

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.fragment_ad_dialog, null, false);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        builder.setView(v);

        // Set typeface for TextViews
        BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");
        BUTLER_BOLD = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Bold.ttf");

        txt_title = (TextView) v.findViewById(R.id.title);
        txt_title.setTypeface(BUTLER_BOLD);

        txt_message = (TextView) v.findViewById(R.id.message);
        txt_message.setTypeface(BUTLER_REG);

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
        btn_yes.setTypeface(BUTLER_BOLD);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss dialog
                dismiss();

                // Show ad
                if (ad.isLoaded()) ad.show();
                else Toast.makeText(getContext(), "No ads available now. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        ad = new InterstitialAd(getContext());
        ad.setAdUnitId(getString(R.string.ad_unit_id));
        ad.loadAd(new AdRequest.Builder().build());

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
