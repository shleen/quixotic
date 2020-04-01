package com.shleen.quixotic;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class WordActivity extends BaseActivity {

    TextView txt_word;
    TextView txt_phonetic;
    ListView lv_definitions;

    DefinitionAdapter adapter;

    Typeface BUTLER_REG;
    Typeface BUTLER_BOLD;

    TextToSpeech tts;

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

        adapter = new DefinitionAdapter(this, R.layout.definition, word.getDefinitions(), word.getExamples());
        lv_definitions.setAdapter(adapter);
    }

    public void readWord(View v) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = tts.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                        tts.speak(txt_word.getText(), TextToSpeech.QUEUE_ADD, null, txt_word.getText().toString());
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Log.i("TTS", "TTS Initialization failed!");
                }
            }
        });
    }
}
