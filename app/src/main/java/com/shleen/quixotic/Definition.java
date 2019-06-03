package com.shleen.quixotic;

public class Definition {
    private String word_type;
    private String definition;

    public Definition() { }

    public Definition(String word_type, String definition) {
        this.word_type = word_type;
        this.definition = definition;
    }

    public String getWord_type() {
        return word_type;
    }

    public void setWord_type(String word_type) {
        this.word_type = word_type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}
