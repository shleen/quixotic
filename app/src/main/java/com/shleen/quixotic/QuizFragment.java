package com.shleen.quixotic;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    int points = 0;
    int rounds = 10;
    private List<Word> selectedWords = new ArrayList<Word>() {};
    private List<Word> words;

    private TextView txt_rounds;
    private TextView txt_definition;
    private TextView txt_question;
    private MaterialButton btn_next;

    private RadioGroup r_grp;

    private RadioButton r_btn_1;
    private RadioButton r_btn_2;
    private RadioButton r_btn_3;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;

    private View view;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_quiz, container, false);

        // Get number of rounds as needed
        if (getArguments()  != null) rounds = getArguments().getInt("rounds", 10);

        Typeface BUTLER_MED = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Medium.ttf");
        Typeface BUTLER_REG = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Butler_Regular.ttf");

        txt_rounds = (TextView) view.findViewById(R.id.txt_rounds);
        txt_definition = (TextView) view.findViewById(R.id.txt_definition);
        txt_question = (TextView) view.findViewById(R.id.txt_question);
        btn_next = (MaterialButton) view.findViewById(R.id.btn_next);

        r_grp = (RadioGroup) view.findViewById(R.id.r_grp);

        r_btn_1 = (RadioButton) view.findViewById(R.id.r_btn_1);
        r_btn_2 = (RadioButton) view.findViewById(R.id.r_btn_2);
        r_btn_3 = (RadioButton) view.findViewById(R.id.r_btn_3);

        txt_rounds.setTypeface(BUTLER_MED);
        txt_definition.setTypeface(BUTLER_REG);
        txt_question.setTypeface(BUTLER_REG);
        btn_next.setTypeface(BUTLER_REG);

        r_btn_1.setTypeface(BUTLER_REG);
        r_btn_2.setTypeface(BUTLER_REG);
        r_btn_3.setTypeface(BUTLER_REG);

        // Get signed-in user
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(view.getContext());

        // Get reference to user's words
        ref = database.getReference(user.getEmail().replaceAll("[^a-zA-Z0-9]", ""));

        // Get words
        words = WordDataHolder.getInstance().getData();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) startQuiz();
    }

    private void startQuiz() {
        // Select words
        Collections.shuffle(words);
        selectedWords = words.subList(0, rounds);

        // Update rounds
        txt_rounds.setText(String.format("%d/%d", rounds-selectedWords.size()+1, rounds));

        List<Word> remaining_words = words.subList(1, words.size()-1);
        Collections.shuffle(remaining_words);

        List<Word> curr_words = Arrays.asList(remaining_words.get(0), remaining_words.get(1), selectedWords.get(0));
        Collections.shuffle(curr_words);

        // Update fields
        r_btn_1.setText(curr_words.get(0).getWord());
        r_btn_2.setText(curr_words.get(1).getWord());
        r_btn_3.setText(curr_words.get(2).getWord());

        txt_definition.setText(selectedWords.get(0).getDefinitions().get(0).getDefinition());

        RadioButton selected_r_btn = ((RadioButton)r_grp.findViewById(r_grp.getCheckedRadioButtonId()));
        selected_r_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMain));
        selected_r_btn.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.default_r_button_tint));
        r_btn_1.setChecked(true);
        btn_next.setText("Check");
    }

    boolean contQuiz(View v) {
        // Update text on btn_next + proceed
        if (btn_next.getText().equals("Check")) {
            // Check answer
            RadioButton selected_r_btn = ((RadioButton)r_grp.findViewById(r_grp.getCheckedRadioButtonId()));
            if (selected_r_btn.getText().equals(selectedWords.get(0).getWord()))
            {
                // Update points
                points += 10;

                // Display message
                Toast.makeText(view.getContext(), "Correct!", Toast.LENGTH_SHORT).show();

                // Color text
                selected_r_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                selected_r_btn.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.green_r_button_tint));
            } else {
                // Display message
                Toast.makeText(view.getContext(), String.format("Wrong :( The answer: %s", selectedWords.get(0).getWord()), Toast.LENGTH_SHORT).show();

                // Color text
                selected_r_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                selected_r_btn.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.red_r_button_tint));
            }
            btn_next.setText("Next >");
        }
        else {
            RadioButton selected_r_btn = ((RadioButton)r_grp.findViewById(r_grp.getCheckedRadioButtonId()));
            selected_r_btn.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMain));
            selected_r_btn.setButtonTintList(ContextCompat.getColorStateList(getContext(), R.color.default_r_button_tint));

            // Check if quiz is over & proceed as needed
            if (selectedWords.size() > 1) {
                // Update selected words & rounds
                selectedWords.remove(0);
                txt_rounds.setText(String.format("%d/%d", rounds-selectedWords.size()+1, rounds));

                // Update fields
                r_btn_1.setChecked(true);

                List<Word> remaining_words = words.subList(rounds, words.size()-1);
                Collections.shuffle(remaining_words);

                List<Word> curr_words = Arrays.asList(remaining_words.get(0), remaining_words.get(1), selectedWords.get(0));
                Collections.shuffle(curr_words);

                r_btn_1.setText(curr_words.get(0).getWord());
                r_btn_2.setText(curr_words.get(1).getWord());
                r_btn_3.setText(curr_words.get(2).getWord());

                txt_definition.setText(selectedWords.get(0).getDefinitions().get(0).getDefinition());

                btn_next.setText("Check");
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
        }
        return false;
    }
}
