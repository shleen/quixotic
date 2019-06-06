package com.shleen.quixotic;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("words");

    EditText edt_add;

    private FirebaseFunctions mFunctions;

    Typeface BUTLER_BOLD;
    TextView txt_word_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide navigation bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        // Set typeface for txt_word_count
        BUTLER_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Butler_Bold.ttf");
        txt_word_count = (TextView) findViewById(R.id.txt_word_count);
        txt_word_count.setTypeface(BUTLER_BOLD);

        setWordCount();

        edt_add = (EditText) findViewById(R.id.edt_add);
    }

    public void goToWords(View v) {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void addWord(final View v) {
        String word = edt_add.getText().toString();

        mFunctions = FirebaseFunctions.getInstance();

        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("word", word);

        mFunctions.getHttpsCallable("addWord")
                  .call(data)
                  .continueWith(new Continuation<HttpsCallableResult, String>() {
                      @Override
                      public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                          // Clear edt_add
                          edt_add.setText("");

                          // Redirect to HomeActivity
                          goToWords(v);

                          // Return result
                          String result = (String) task.getResult().getData();
                          return result;
                    }
                });
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
}