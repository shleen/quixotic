package com.shleen.quixotic;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class QuizLandingFragment extends Fragment {

    TextView txt_caption;
    MaterialButton btn_play;

    Typeface BUTLER_MED;

    View view;

    public QuizLandingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_quiz_landing, container, false);

        txt_caption = (TextView) view.findViewById(R.id.txt_caption);
        btn_play = (MaterialButton) view.findViewById(R.id.btn_play);

        BUTLER_MED = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Medium.ttf");

        txt_caption.setTypeface(BUTLER_MED);
        btn_play.setTypeface(BUTLER_MED);

        return view;
    }

}
