package com.shleen.quixotic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Word implements Parcelable {
    private String word;
    private String phonetic;
    private ArrayList<Definition> definitions;
    private String created_on;
    private ArrayList<String> examples;

    public Word() { }

    public Word(String word, String phonetic, ArrayList<Definition> definitions, String created_on, ArrayList<String> examples) {
        this.word = word;
        this.phonetic = phonetic;
        this.definitions = definitions;
        this.created_on = created_on;
        this.examples = examples;
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

    public String getCreatedOn() {
        return created_on;
    }

    public void setCreatedOn(String created_on) {
        this.created_on = created_on;
    }

    public ArrayList<String> getExamples() {
        return examples;
    }

    public void setExamples(ArrayList<String> examples) {
        this.examples = examples;
    }

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(word);
        out.writeString(phonetic);
        out.writeList(definitions);
        out.writeList(examples);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Word(Parcel in) {
        this.word = in.readString();
        this.phonetic = in.readString();

        this.definitions = new ArrayList<>();
        in.readList(this.definitions, Definition.class.getClassLoader());

        this.examples = in.readArrayList(ClassLoader.getSystemClassLoader());
    }
}
