package com.shleen.quixotic;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;

public class QuizEndFragment extends Fragment {

    TextView txt_points_caption;
    TextView txt_points;
    TextView txt_congratulations;
    TextView txt_score;
    MaterialButton btn_play_again;

    Typeface BUTLER_REG;
    Typeface BUTLER_MED;
    Typeface BUTLER_BOLD;

    View view;

    InterstitialAd ad;

    public QuizEndFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ad = new InterstitialAd(context);
        ad.setAdUnitId(getString(R.string.ad_unit_id));
        ad.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_quiz_end, container, false);

        BUTLER_REG = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Regular.ttf");
        BUTLER_MED = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Medium.ttf");
        BUTLER_BOLD = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Bold.ttf");

        txt_points_caption = (TextView) view.findViewById(R.id.txt_points_caption);
        txt_points_caption.setTypeface(BUTLER_REG);

        txt_points = (TextView) view.findViewById(R.id.txt_points);
        txt_points.setTypeface(BUTLER_MED);

        txt_congratulations = (TextView) view.findViewById(R.id.txt_congratulations);
        txt_congratulations.setTypeface(BUTLER_BOLD);

        txt_score = (TextView) view.findViewById(R.id.txt_score);
        txt_score.setTypeface(BUTLER_MED);

        btn_play_again = (MaterialButton) view.findViewById(R.id.btn_play_again);
        btn_play_again.setTypeface(BUTLER_REG);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (ad.isLoaded()) ad.show();

            txt_points.setText(Integer.toString(getArguments().getInt("points")));
            if (getArguments().getInt("points") == 0) {
                txt_congratulations.setText("Oh no :(");
            } else if (getArguments().getInt("points") < 50) {
                txt_congratulations.setText("Hm! Not bad ...");
            }
            txt_score.setText(String.format("You got %d out of %d questions correct.", getArguments().getInt("points")/10, getArguments().getInt("rounds")));
        }

        super.onHiddenChanged(hidden);
    }
}
