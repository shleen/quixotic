package com.shleen.quixotic;

class Note {
    String note;
    String author;
    String work;
    String created_on;

    public Note(String note, String author, String work, String created_on) {
        this.note = note;
        this.author = author;
        this.work = work;
        this.created_on = created_on;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
