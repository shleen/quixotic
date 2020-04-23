package com.shleen.quixotic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import static android.widget.Toast.LENGTH_SHORT;

public class QuizLandingActivity extends BaseActivity {

    TextView txt_caption;
    MaterialButton btn_play;

    Typeface BUTLER_MED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_landing);

        txt_caption = (TextView) findViewById(R.id.txt_caption);
        btn_play = (MaterialButton) findViewById(R.id.btn_play);

        BUTLER_MED = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Butler_Medium.ttf");

        txt_caption.setTypeface(BUTLER_MED);
        btn_play.setTypeface(BUTLER_MED);

        // Set nav listeners
        setNavListeners();
    }

}
