package com.shleen.quixotic;

import java.util.Comparator;

public class WordSorter implements Comparator<Word> {
    public int compare(Word w1, Word w2) {
        return (w1.getWord().compareTo(w2.getWord()));
    }
}
