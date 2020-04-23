package com.shleen.quixotic;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class QuizEndActivity extends BaseActivity {

    TextView txt_points_caption;
    TextView txt_points;
    TextView txt_congratulations;
    TextView txt_score;
    MaterialButton btn_play_again;

    Typeface BUTLER_REG;
    Typeface BUTLER_MED;
    Typeface BUTLER_BOLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_end);

        BUTLER_REG = Typeface.createFromAsset(getAssets(), "fonts/Butler_Regular.ttf");
        BUTLER_MED = Typeface.createFromAsset(getAssets(), "fonts/Butler_Medium.ttf");
        BUTLER_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Butler_Bold.ttf");

        txt_points_caption = (TextView) findViewById(R.id.txt_points_caption);
        txt_points_caption.setTypeface(BUTLER_REG);

        txt_points = (TextView) findViewById(R.id.txt_points);
        txt_points.setTypeface(BUTLER_MED);
        txt_points.setText(Integer.toString(getIntent().getIntExtra("points", 0)));

        txt_congratulations = (TextView) findViewById(R.id.txt_congratulations);
        txt_congratulations.setTypeface(BUTLER_BOLD);
        if (getIntent().getIntExtra("points", 0) == 0) {
            txt_congratulations.setText("Oh no :(");
        } else if (getIntent().getIntExtra("points", 0) < 50) {
            txt_congratulations.setText("Hm! Not bad ...");
        }

        txt_score = (TextView) findViewById(R.id.txt_score);
        txt_score.setTypeface(BUTLER_MED);
        txt_score.setText(String.format("You got %d out of %d questions correct.", getIntent().getIntExtra("points", 0)/10, getIntent().getIntExtra("rounds", 0)));

        btn_play_again = (MaterialButton) findViewById(R.id.btn_play_again);
        btn_play_again.setTypeface(BUTLER_REG);

        // Set nav listeners
        setNavListeners();
    }

}
