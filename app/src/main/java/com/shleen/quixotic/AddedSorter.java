package com.shleen.quixotic;

import java.util.Comparator;

public class AddedSorter implements Comparator<Word> {
    public int compare(Word w1, Word w2) {
        return w2.getCreatedOn().compareTo(w1.getCreatedOn());
    }
}
