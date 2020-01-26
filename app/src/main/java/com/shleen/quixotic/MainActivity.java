package com.shleen.quixotic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    SQLiteDatabase db;

    EditText edt_add;

    Typeface BUTLER_REG;
    TextView txt_word_count;
    TextView txt_user_name;

    List<Word> words;

    static boolean sort_alphabetically = false;

    Gson gson;

    static GoogleSignInAccount user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize WordDataHolder
        WordDataHolder.setInstance(new WordDataHolder(this));

        // Hide navigation bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Get signed-in user
        user = GoogleSignIn.getLastSignedInAccount(this);

        // Get reference to user's words
        ref = database.getReference(String.format("%s/words", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

        setWordCount();

        // Set typeface for txt_word_count & txt_user_name
        BUTLER_REG = Typeface.createFromAsset(getAssets(), "fonts/Butler_Regular.ttf");
        txt_word_count = (TextView) findViewById(R.id.txt_word_count);
        txt_word_count.setTypeface(BUTLER_REG);

        // Set user name
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);
        txt_user_name.setTypeface(BUTLER_REG);
        txt_user_name.setText( String.format("Hello, %s.", user.getDisplayName()));


        edt_add = (EditText) findViewById(R.id.edt_add);

        // Set typeface for edt_add
        edt_add.setTypeface(BUTLER_REG);

        // Pre-load words
        WordDataHolder.getInstance().setData();
        words = WordDataHolder.getInstance().getData();

        // Initialize gson
        gson = new Gson();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Update word count
        setWordCount();

        // Initialize database on first run of application
        SharedPreferences settings = getSharedPreferences("FIRST_LAUNCH", 0);
        if (settings.getBoolean("FIRST_LAUNCH", true)) {

            initialiseDatabase(this);

            // Update FIRST_LAUNCH
            settings.edit().putBoolean("FIRST_LAUNCH", false).apply();
        }

    }

    public void goToWords(View v) {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void addWord(final View v) {
        // Pull word to add from edt_add
        final String word = edt_add.getText().toString().toLowerCase();

        // Check if the word already exists
        Util u = new Util();
        if (u.getWords(words).contains(word)) {
            // Word already exists. Alert user, then redirect to the request word's page.
            Toast.makeText(getApplicationContext(),word + " has already been added.",Toast.LENGTH_SHORT).show();

            // Navigate to word-specific page
            Intent i = new Intent(v.getContext(), WordActivity.class);
            i.putExtra("WORD", words.get(u.getWord(word, words)));

            v.getContext().startActivity(i);

            return;
        }

        // Pull word from WordsAPI.
        try {
            // Create the request
            URL url = new URL(String.format("https://wordsapiv1.p.rapidapi.com/words/%s", word));
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Set headers
            con.setRequestProperty("x-rapidapi-host", "wordsapiv1.p.rapidapi.com");
            con.setRequestProperty("x-rapidapi-key", "39d1a51539msh98cb55556e92c0bp12dd60jsnb3492d0c0071");

            con.setRequestMethod("GET");

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Send the request
                        int status = con.getResponseCode();

                        // Read the response
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuffer content = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        in.close();

                        // Get word features
                        WordRes word = gson.fromJson(content.toString(), WordRes.class);

                        // Push word to Firebase
                        Map<String, Object> word_info = new HashMap<>();
                        word_info.put(word.getWord(), new Word(word.getWord(),  word.getPronunciation(), word.getDefinitions(), Long.toString(Instant.now().getEpochSecond())));

                        ref.updateChildren(word_info);

                        // Close the connection
                        con.disconnect();

                        // Clear edt_add
                        edt_add.setText("");

                        // Redirect to HomeActivity
                        goToWords(v);
                    }
                    catch (IOException e) {
                        // TODO: Handle IOException
                    }
                }
            });

        }
        catch (MalformedURLException e) {
            // TODO: Handle MalformedURLException
        }
        catch (IOException e) {
            // TODO: Handle IOException
        }

    }

    private void setWordCount() {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txt_word_count.setText(String.format(Locale.ENGLISH, "%d words added.", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Handle database error
            }
        });
    }

    private void initialiseDatabase(final Context c) {

        // Create & populate database in a new thread
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                db = Util.getDb(c);

                try {
                    InputStream inputstream = getAssets().open("create_database.sql");
                    String[] statements = Util.parseSqlFile(inputstream);

                    for (String statement : statements) { db.execSQL(statement); }
                    inputstream.close();

                } catch (IOException e) {
                    // TODO: Handle IOException
                }

                db.close();
            }
        });
        thread.start();
    }

}