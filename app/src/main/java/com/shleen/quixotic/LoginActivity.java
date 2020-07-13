package com.shleen.quixotic;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Typeface BUTLER_REG;
    Typeface BUTLER_MED;
    TextView txt_welcome;
    TextView txt_sign_in_w_google;

    GoogleSignInClient mGoogleSignInClient;
    static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {
            // Show UI
            setContentView(R.layout.activity_login);

            // Set typeface for TextViews
            BUTLER_REG = Typeface.createFromAsset(getAssets(), "fonts/Butler_Regular.ttf");
            BUTLER_MED = Typeface.createFromAsset(getAssets(), "fonts/Butler_Medium.ttf");

            txt_welcome = (TextView) findViewById(R.id.txt_welcome);
            txt_welcome.setTypeface(BUTLER_MED);

            txt_sign_in_w_google = (TextView) findViewById(R.id.txt_sign_in_w_google);
            txt_sign_in_w_google.setTypeface(BUTLER_REG);

            findViewById(R.id.sign_in_button).setOnClickListener(this);
        }
        else updateUI(account);
    }

    private void updateUI(GoogleSignInAccount acc)
    {
        if (acc != null) {
            // Existing sign-in found. Redirect to home page.
            Intent i = new Intent(getApplicationContext(), BaseActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) { signIn(); }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                updateUI(task.getResult());
            } catch (Exception ignored) { }
        }
    }

}
