package com.shleen.quixotic;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Util {
    Context myContext;

    public Util(Context context) {
        this.myContext = context;
    }

    // Returns the position of the given word (String) in the given list of words
    public int getWord(String word, List<Word> words) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getWord().equals(word)) {
                return i;
            }
        }
        return -1;
    }

    // Returns a list of strings, of only the word attribute of the given list of word objects
    public List<String> getWords(List<Word> w) {

        List<String> out = new ArrayList<>();
        for (int i = 0; i < w.size(); i++) {
            out.add(w.get(i).getWord());
        }

        return out;
    }

}
