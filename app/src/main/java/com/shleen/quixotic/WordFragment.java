package com.shleen.quixotic;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class WordFragment extends Fragment {

    TextView txt_word;
    TextView txt_phonetic;
    MaterialButton btn_pronounce;
    ListView lv_definitions;

    DefinitionAdapter adapter;

    Typeface BUTLER_REG;
    Typeface BUTLER_BOLD;

    TextToSpeech tts;

    View view;

    Word word;

    public WordFragment(Word word) {
        this.word = word;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_word, container, false);

        // Create typeface required
        BUTLER_REG = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Regular.ttf");
        BUTLER_BOLD = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Bold.ttf");

        // Initialise views
        txt_word = (TextView) view.findViewById(R.id.word);
        txt_phonetic = (TextView) view.findViewById(R.id.phonetic);
        btn_pronounce = (MaterialButton) view.findViewById(R.id.pronounce);
        lv_definitions = (ListView) view.findViewById(R.id.definitions);

        // Display word details
        txt_word.setTypeface(BUTLER_BOLD);
        txt_word.setText(word.getWord());

        txt_phonetic.setTypeface(BUTLER_REG);
        txt_phonetic.setText(word.getPhonetic());

        adapter = new DefinitionAdapter(view.getContext(), R.layout.definition, word.getDefinitions(), word.getExamples());
        lv_definitions.setAdapter(adapter);

        btn_pronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readWord(v);
            }
        });

        return view;
    }

    public void readWord(View v) {
        tts = new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
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
