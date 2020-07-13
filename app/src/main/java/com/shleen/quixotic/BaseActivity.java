package com.shleen.quixotic;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class BaseActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    final Fragment homeFragment = new HomeFragment();
    final Fragment notesFragment = new NotesFragment();
    final Fragment mainFragment = new MainFragment();
    final Fragment quizLandingFragment = new QuizLandingFragment();
    final Fragment quizFragment = new QuizFragment();
    final Fragment quizEndFragment = new QuizEndFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = mainFragment;

    List<Word> words;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            // Initialize WordDataHolder
            WordDataHolder.setInstance(new WordDataHolder(this));

            // Initialize NoteDataHolder
            NoteDataHolder.setInstance(new NoteDataHolder(this));

            // Pre-load words
            WordDataHolder.getInstance().setData();
            words = WordDataHolder.getInstance().getData();

            // Pre-load notes
            NoteDataHolder.getInstance().setData();
        }

        fm.beginTransaction().add(R.id.main_container, mainFragment).commit();
        fm.beginTransaction().add(R.id.main_container, homeFragment).hide(homeFragment).commit();
        fm.beginTransaction().add(R.id.main_container, notesFragment).hide(notesFragment).commit();
        fm.beginTransaction().add(R.id.main_container, quizLandingFragment).hide(quizLandingFragment).commit();
        fm.beginTransaction().add(R.id.main_container, quizFragment).hide(quizFragment).commit();
        fm.beginTransaction().add(R.id.main_container, quizEndFragment).hide(quizEndFragment).commit();
        fm.beginTransaction().add(R.id.main_container, profileFragment).hide(profileFragment).commit();

        // Setup bottom nav
        setNavListeners();
    }

    protected void setNavListeners() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.false_add);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        return true;
                    case R.id.notes:
                        fm.beginTransaction().hide(active).show(notesFragment).commit();
                        active = notesFragment;
                        return true;
                    case R.id.quiz:
                        fm.beginTransaction().hide(active).show(quizLandingFragment).commit();
                        active = quizLandingFragment;
                        return true;
                    case R.id.profile:
                        fm.beginTransaction().hide(active).show(profileFragment).commit();
                        active = profileFragment;
                        return true;
                }
                return false;
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Deselect bottom nav
                bottomNavigationView.setSelectedItemId(R.id.false_add);

                // Navigate
                fm.beginTransaction().hide(active).show(mainFragment).commit();
                active = mainFragment;
            }
        });
    }

    public void startQuiz(View v) {

        // TODO: Respond to users without any words
        if (WordDataHolder.getInstance().getData().size() == 0) {
            Toast.makeText(getApplicationContext(), "Try adding some words to your collection first!", LENGTH_SHORT).show();
        } else {

            Bundle bundle = new Bundle();
            bundle.putInt("rounds", 10);
            quizFragment.setArguments(bundle);

            fm.beginTransaction().hide(active).show(quizFragment).commit();
            active = quizFragment;
        }

    }

    public void hideKeyboard(View v) {
        InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void goToWords(final View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Select nav item
                bottomNavigationView.setSelectedItemId(R.id.home);

                hideKeyboard(v);

                // Navigate
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
            }
        });
    }

    public void goToWord(Word word) {
        WordFragment wordFragment = new WordFragment(word);
        fm.beginTransaction().hide(active).add(R.id.main_container, wordFragment).commit();
        active = wordFragment;
    }

    public void contQuiz(View v) {
        if (((QuizFragment)quizFragment).contQuiz(v)) {
            // Navigate to quiz end fragment
            Bundle bundle = new Bundle();
            bundle.putInt("points", ((QuizFragment)quizFragment).points);
            bundle.putInt("rounds", ((QuizFragment)quizFragment).rounds);
            quizEndFragment.setArguments(bundle);

            hideKeyboard(v);

            fm.beginTransaction().hide(active).show(quizEndFragment).commit();
            active = quizEndFragment;
        }
    }
}
