package com.shleen.quixotic;

import java.util.ArrayList;

public class Word {
    private String word;
    private String phonetic;
    private ArrayList<Definition> definitions;

    public Word() { }

    public Word(String word, String phonetic, ArrayList<Definition> definitions) {
        this.word = word;
        this.phonetic = phonetic;
        this.definitions = definitions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public ArrayList<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(ArrayList<Definition> definitions) {
        this.definitions = definitions;
    }
}
