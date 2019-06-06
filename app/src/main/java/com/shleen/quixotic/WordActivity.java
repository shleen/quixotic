package com.shleen.quixotic;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class WordActivity extends AppCompatActivity {

    TextView txt_word;
    ListView lv_definitions;

    DefinitionAdapter adapter;

    Typeface BUTLER_BOLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        // Create typeface required
        BUTLER_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Butler_Bold.ttf");

        // Initialise views
        txt_word = (TextView) findViewById(R.id.word);
        lv_definitions = (ListView) findViewById(R.id.definitions);

        // Get word passed through
        Word word = (Word) getIntent().getParcelableExtra("WORD");

        // Display word details
        txt_word.setTypeface(BUTLER_BOLD);
        txt_word.setText(word.getWord());

        adapter = new DefinitionAdapter(this, R.layout.definition, word.getDefinitions());
        lv_definitions.setAdapter(adapter);
    }
}
