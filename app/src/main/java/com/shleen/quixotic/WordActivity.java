package com.shleen.quixotic;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class WordActivity extends AppCompatActivity {

    TextView txt_word;
    TextView txt_phonetic;
    ListView lv_definitions;

    DefinitionAdapter adapter;

    Typeface BUTLER_REG;
    Typeface BUTLER_BOLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        // Create typeface required
        BUTLER_REG = Typeface.createFromAsset(getAssets(), "fonts/Butler_Regular.ttf");
        BUTLER_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Butler_Bold.ttf");

        // Initialise views
        txt_word = (TextView) findViewById(R.id.word);
        txt_phonetic = (TextView) findViewById(R.id.phonetic);
        lv_definitions = (ListView) findViewById(R.id.definitions);

        // Get word passed through
        Word word = (Word) getIntent().getParcelableExtra("WORD");

        // Display word details
        txt_word.setTypeface(BUTLER_BOLD);
        txt_word.setText(word.getWord());

        txt_phonetic.setTypeface(BUTLER_REG);
        txt_phonetic.setText(word.getPhonetic());

        adapter = new DefinitionAdapter(this, R.layout.definition, word.getDefinitions());
        lv_definitions.setAdapter(adapter);
    }
}
