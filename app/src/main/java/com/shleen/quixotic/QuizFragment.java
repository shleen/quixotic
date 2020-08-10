package com.shleen.quixotic;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    int points = 0;
    int rounds = 10;
    List<Word> selectedWords = new ArrayList<Word>() {};

    TextView txt_rounds;
    TextView txt_definition;
    TextView txt_question;
    TextInputEditText edt_answer;

    Typeface BUTLER_MED;
    Typeface BUTLER_REG;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    View view;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Get number of rounds as needed
        if (getArguments()  != null) rounds = getArguments().getInt("rounds", 10);

        BUTLER_MED = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Medium.ttf");
        BUTLER_REG = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Regular.ttf");

        txt_rounds = (TextView) view.findViewById(R.id.txt_rounds);
        txt_definition = (TextView) view.findViewById(R.id.txt_definition);
        txt_question = (TextView) view.findViewById(R.id.txt_question);
        edt_answer = (TextInputEditText) view.findViewById(R.id.edt_answer);

        txt_rounds.setTypeface(BUTLER_MED);
        txt_definition.setTypeface(BUTLER_REG);
        txt_question.setTypeface(BUTLER_REG);
        edt_answer.setTypeface(BUTLER_REG);

        // Get signed-in user
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(view.getContext());

        // Get reference to user's words
        ref = database.getReference(user.getEmail().replaceAll("[^a-zA-Z0-9]", ""));

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) { startQuiz(); }

    }

    public void startQuiz() {

        // Update rounds
        txt_rounds.setText(String.format("%d/%d", rounds-selectedWords.size()+1, rounds));

        // Get words
        List<Word> words = WordDataHolder.getInstance().getData();

        // Select words
        rounds = Math.min(rounds, words.size());
        Collections.shuffle(words);
        selectedWords = words.subList(0, rounds);

        // Update fields
        txt_definition.setText(selectedWords.get(0).getDefinitions().get(0).getDefinition());

    }

    public boolean contQuiz(View v) {

        // Check answer
        if (edt_answer.getText().toString().equals(selectedWords.get(0).getWord())) {
            // Update points
            points += 10;

            // Display message
            Toast.makeText(view.getContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Display message
            Toast.makeText(view.getContext(), String.format("Wrong :( The answer: %s", selectedWords.get(0).getWord()), Toast.LENGTH_SHORT).show();
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

            // Return true to signal end of quiz
            return true;

        }
        return false;
    }
}
