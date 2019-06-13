package com.shleen.quixotic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shleen.quixotic.MainActivity.sort_alphabetically;

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

    // Sorts the given list of words as indicated by the sort_alphabetically static flag
    public List<Word> sortWords(List<Word> words) {
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

    // Returns the current database
    public static SQLiteDatabase getDb(Context c) {
        return SQLiteDatabase.openOrCreateDatabase(c.getDatabasePath("db").getAbsolutePath(), null);
    }

    // Parses a given sql file as an input stream & returns an array of statements
    public static String[] parseSqlFile(InputStream file) throws IOException {
        BufferedReader sqlFile = new BufferedReader(new InputStreamReader(file));

        String line;
        StringBuilder sql = new StringBuilder();
        String multiLineComment = null;

        while ((line = sqlFile.readLine()) != null) {
            line = line.trim();

            // Check for start of multi-line comment
            if (multiLineComment == null) {
                // Check for first multi-line comment type
                if (line.startsWith("/*")) {
                    if (!line.endsWith("}")) {
                        multiLineComment = "/*";
                    }
                    // Check for second multi-line comment type
                } else if (line.startsWith("{")) {
                    if (!line.endsWith("}")) {
                        multiLineComment = "{";
                    }
                    // Append line if line is not empty or a single line comment
                } else if (!line.startsWith("--") && !line.equals("")) {
                    sql.append(line);
                } // Check for matching end comment
            } else if (multiLineComment.equals("/*")) {
                if (line.endsWith("*/")) {
                    multiLineComment = null;
                }
                // Check for matching end comment
            } else if (multiLineComment.equals("{")) {
                if (line.endsWith("}")) {
                    multiLineComment = null;
                }
            }

        }

        sqlFile.close();

        return sql.toString().split(";");
    }
}
