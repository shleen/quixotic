package com.shleen.quixotic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BaseActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void setNavListeners() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {
                    case R.id.home:
                        i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        break;
                    case R.id.notes:
                        Toast.makeText(getApplicationContext(), "Notes. Coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.quiz:
                        Toast.makeText(getApplicationContext(), "Quiz. Coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
