package com.shleen.quixotic;

import java.util.ArrayList;

public class WordRes {
    private String word;
    private ArrayList<Result> results;
    private Syllables syllables;
    private Pronunciation pronunciation;

    public String getWord() { return word; }

    public String getPronunciation() { return pronunciation.getAll(); }

    public ArrayList<Definition> getDefinitions() {
        ArrayList<Definition> definitions = new ArrayList<>();

        for (Result r : results) {
            definitions.add(new Definition(r.getPartOfSpeech(), r.getDefinition()));
        }

        return definitions;
    }

    public ArrayList<String> getExamples() {
        ArrayList<String> examples = new ArrayList<>();

        for (Result r : results) {
            if (r.getExamples() == null) {
                examples.add("");
            }
            else {
                for (String e : r.getExamples()) {
                    examples.add(e);
                }
            }
        }

        return examples;
    }

    public ArrayList<Result> getResults() { return results; }
}

class Result {
    private String definition;
    private String partOfSpeech;
    private ArrayList<String> synonyms;
    private ArrayList<String> typeOf;
    private ArrayList<String> hasTypes;
    private ArrayList<String> derivation;
    private ArrayList<String> examples;

    public String getDefinition() { return definition; }

    public String getPartOfSpeech() { return partOfSpeech; }

    public ArrayList<String> getExamples() { return examples; }
}

class Syllables {
    private int count;
    private ArrayList<String> list;
}

class Pronunciation {
    private String all;

    public String getAll() { return all; }
}