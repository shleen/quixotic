package com.shleen.quixotic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

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

        // Get signed-in user
        user = GoogleSignIn.getLastSignedInAccount(this);

        // Get reference to user's words
        ref = database.getReference(String.format("%s/words", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")));

        // Set up listener to update word count
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                txt_word_count.setText(String.format(Locale.ENGLISH, "%d words added.", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // TODO: Handle database error
            }
        });

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

        // Set enter listener for edt_add
        edt_add.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                addWord(edt_add);
                return true;
            }
        });

        // Pre-load words
        WordDataHolder.getInstance().setData();
        words = WordDataHolder.getInstance().getData();

        // Initialize gson
        gson = new Gson();

        // Set nav listeners
        setNavListeners();

        // Deselect nav items
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.false_add);
    }

    public void goToWords(View v) {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void addWord(final View v) {

        // Pull word to add from edt_add
        final String word = edt_add.getText().toString().toLowerCase();

        // Check that there is input to handle
        if (word.equals("")) { return; }

        // Check if the word already exists
        Util u = new Util();
        if (u.getWords(words).contains(word)) {
            // Word already exists. Alert user, then redirect to the request word's page.
            Toast.makeText(getApplicationContext(),word + " has already been added.",Toast.LENGTH_SHORT).show();

            // Navigate to word-specific page
            Intent i = new Intent(v.getContext(), WordActivity.class);
            i.putExtra("WORD", words.get(u.getWord(word, words)));

            v.getContext().startActivity(i);
        }
        else {

            // Word doesn't exist. Get API credentials & Pull word from WordsAPI.
            DatabaseReference cred_ref = database.getReference("/creds");
            cred_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        // Create the request
                        URL url = new URL(String.format("https://wordsapiv1.p.rapidapi.com/words/%s", word));
                        final HttpURLConnection con = (HttpURLConnection) url.openConnection();

                        // Set headers
                        con.setRequestProperty("x-rapidapi-host", "wordsapiv1.p.rapidapi.com");
                        con.setRequestProperty("x-rapidapi-key", Objects.requireNonNull(dataSnapshot.child("key").getValue()).toString());

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
                                    GsonBuilder gsonBuilder = new GsonBuilder();

                                    WordResDeserializer deserializer = new WordResDeserializer();
                                    gsonBuilder.registerTypeAdapter(WordRes.class, deserializer);

                                    Gson customGson = gsonBuilder.create();
                                    WordRes word = customGson.fromJson(content.toString(), WordRes.class);

                                    if (word == null) throw new IOException();

                                    // Push word to Firebase
                                    Map<String, Object> word_info = new HashMap<>();
                                    word_info.put(word.getWord(), new Word(word.getWord(),  word.getPronunciation(), word.getDefinitions(), Long.toString(Instant.now().getEpochSecond()), word.getExamples()));

                                    ref.updateChildren(word_info);

                                    // Close the connection
                                    con.disconnect();

                                    // Redirect to HomeActivity
                                    goToWords(v);
                                }
                                catch (IOException e) {
                                    // TODO: Handle IOException
                                    NoResultDialog noResultDialog = new NoResultDialog();
                                    noResultDialog.showNoResultDialog(getSupportFragmentManager(), "noResultDialog", word);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // TODO: Handle database failure
                }
            });

        }

        // Clear edt_add
        edt_add.setText("");

    }

}