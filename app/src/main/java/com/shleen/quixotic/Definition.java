package com.shleen.quixotic;

import android.os.Parcel;
import android.os.Parcelable;

public class Definition implements Parcelable {
    private String word_type;
    private String definition;

    public Definition() { }

    public Definition(String word_type, String definition) {
        this.word_type = word_type;
        this.definition = definition;
    }

    // Parcelable constructor: takes a Parcel & returned a populated object
    private Definition(Parcel in) {
        this.word_type = in.readString();
        this.definition = in.readString();
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

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(word_type);
        out.writeString(definition);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Definition> CREATOR = new Parcelable.Creator<Definition>() {
        public Definition createFromParcel(Parcel in) {
            return new Definition(in);
        }

        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };
}
