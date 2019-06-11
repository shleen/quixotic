package com.shleen.quixotic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shleen.quixotic.HomeActivity.sort_by_created;

public class Util {

    public Util() { }

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

    // Sorts the given list of words as indicated by the sort_by_created static flag
    public List<Word> sortWords(List<Word> words) {
        if (sort_by_created) {
            // Sort by recently added
            Collections.sort(words, new WordSorter());
            sort_by_created = false;
        } else {
            // Sort alphabetically
            Collections.sort(words, new AddedSorter());
            sort_by_created = true;
        }

        return words;
    }

}
