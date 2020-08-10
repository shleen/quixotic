package com.shleen.quixotic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shleen.quixotic.MainFragment.sort_alphabetically;

class Util {

    Util() { }

    // Returns the position of the given word (String) in the given list of words
    int getWord(String word, List<Word> words) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getWord().equals(word)) {
                return i;
            }
        }
        return -1;
    }

    // Returns a list of strings, of only the word attribute of the given list of word objects
    List<String> getWords(List<Word> w) {

        List<String> out = new ArrayList<>();
        for (int i = 0; i < w.size(); i++) {
            out.add(w.get(i).getWord());
        }

        return out;
    }

    // Sorts the given list of words as indicated by the sort_alphabetically static flag
    List<Word> sortWords(List<Word> words) {
        if (sort_alphabetically) {
            // Sort alphabetically
            Collections.sort(words, new AddedSorter());
            sort_alphabetically = false;
        } else {
            // Sort by recently added
            Collections.sort(words, new WordSorter());
            sort_alphabetically = true;
        }

        return words;
    }

}
