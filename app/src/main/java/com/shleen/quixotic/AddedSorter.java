package com.shleen.quixotic;

import java.util.Comparator;

public class AddedSorter implements Comparator<Word> {
    public int compare(Word w1, Word w2) {
        return w1.getCreatedOn().compareTo(w2.getCreatedOn());
    }
}
