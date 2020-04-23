package com.shleen.quixotic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class QuizActivity extends BaseActivity {

    int points = 0;

    private int rounds;
    List<Word> selectedWords;

    TextView txt_rounds;
    TextView txt_definition;
    TextView txt_question;
    TextInputEditText edt_answer;

    Typeface BUTLER_MED;
    Typeface BUTLER_REG;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get number of rounds as needed
        rounds = getIntent().getIntExtra("rounds", 10);

        BUTLER_MED = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Butler_Medium.ttf");
        BUTLER_REG = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Butler_Regular.ttf");

        txt_rounds = (TextView) findViewById(R.id.txt_rounds);
        txt_definition = (TextView) findViewById(R.id.txt_definition);
        txt_question = (TextView) findViewById(R.id.txt_question);
        edt_answer = (TextInputEditText) findViewById(R.id.edt_answer);

        txt_rounds.setTypeface(BUTLER_MED);
        txt_definition.setTypeface(BUTLER_REG);
        txt_question.setTypeface(BUTLER_REG);
        edt_answer.setTypeface(BUTLER_REG);

        startQuiz();

        // Set nav listeners
        setNavListeners();

        // Get signed-in user
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(this);

        // Get reference to user's words
        ref = database.getReference(user.getEmail().replaceAll("[^a-zA-Z0-9]", ""));
    }

    public void startQuiz() {

        // Get words
        List<Word> words = WordDataHolder.getInstance().getData();

        // Select words
        rounds = Math.min(rounds, words.size());
        Collections.shuffle(words);
        selectedWords = words.subList(0, rounds);

        // Update fields
        txt_definition.setText(selectedWords.get(0).getDefinitions().get(0).getDefinition());

    }

    public void contQuiz(View v) {

        // Check answer
        if (edt_answer.getText().toString().equals(selectedWords.get(0).getWord())) {
            // Update points
            points += 10;

            // Display message
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Display message
            Toast.makeText(getApplicationContext(), String.format("Wrong :( The answer: %s", selectedWords.get(0).getWord()), Toast.LENGTH_SHORT).show();
        }

        // Check if quiz is over & proceed as needed
        if (selectedWords.size() > 1) {
            // Update selected words & rounds
            selectedWords.remove(0);
            txt_rounds.setText(String.format("%d/%d", rounds-selectedWords.size()+1, rounds));

            // Update fields
            edt_answer.setText("");
            txt_definition.setText(selectedWords.get(0).getDefinitions().get(0).getDefinition());
        } else {

            // Update player's points
            ref.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        // Existing points found. Increment.
                        ref.child("points").setValue(Integer.parseInt(dataSnapshot.getValue().toString()) + points);
                    }
                    catch (NullPointerException e) {
                        // No existing points found. Set.
                        ref.child("points").setValue(points);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // TODO: Handle database error
                }
            });

            // Navigate to quiz end activity
            Intent i = new Intent(getApplicationContext(), QuizEndActivity.class);
            i.putExtra("points", points);
            i.putExtra("rounds", rounds);
            startActivity(i);

        }

    }
}
