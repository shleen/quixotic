package com.shleen.quixotic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
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

public class MainFragment extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    EditText edt_add;
    MaterialButton btn_add;

    Typeface BUTLER_REG;
    TextView txt_word_count;
    TextView txt_user_name;

    private WordListAdaptor wordListAdaptor;
    List<Word> words;

    static boolean sort_alphabetically = false;

    Gson gson;

    static GoogleSignInAccount user = null;

    View view;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_main, container, false);

        // Get signed-in user
        user = GoogleSignIn.getLastSignedInAccount(view.getContext());

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
        BUTLER_REG = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Regular.ttf");
        txt_word_count = (TextView) view.findViewById(R.id.txt_word_count);
        txt_word_count.setTypeface(BUTLER_REG);

        // Set user name
        txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
        txt_user_name.setTypeface(BUTLER_REG);
        txt_user_name.setText( String.format("Hello, %s.", user.getDisplayName()));

        edt_add = (EditText) view.findViewById(R.id.edt_add);

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

        wordListAdaptor = WordDataHolder.getInstance().getWordListAdaptor();
        words = wordListAdaptor.getWords();

        btn_add = (MaterialButton) view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWord(v);
            }
        });

        // Initialize gson
        gson = new Gson();

        return view;
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
            Toast.makeText(view.getContext(),word + " has already been added.",Toast.LENGTH_SHORT).show();

            // Navigate to word-specific page
            ((BaseActivity) getActivity()).goToWord(words.get(u.getWord(word, words)));
//            Intent i = new Intent(v.getContext(), WordActivity.class);
//            i.putExtra("WORD", words.get(u.getWord(word, words)));
//
//            v.getContext().startActivity(i);
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
                                    ((BaseActivity)getActivity()).goToWords(v);
                                }
                                catch (IOException e) {
                                    // TODO: Handle IOException
                                    NoResultDialog noResultDialog = new NoResultDialog();
                                    noResultDialog.showNoResultDialog(getFragmentManager(), "noResultDialog", word);
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