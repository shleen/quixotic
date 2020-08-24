package com.shleen.quixotic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    GoogleSignInClient mGoogleSignInClient;

    ImageView img_ad;
    ImageView img_user;
    MaterialButton btn_logout;

    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Set img_user
        img_user = (ImageView) view.findViewById(R.id.img_user);
        String img_user_url = GoogleSignIn.getLastSignedInAccount(getContext()).getPhotoUrl().toString();

        Glide.with(this).load(img_user_url).into(img_user);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Redirect to LoginActivity & clear navigation stack
                        Intent i = new Intent(view.getContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
            }
        });

        img_ad = view.findViewById(R.id.img_ad);
        img_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdDialog adDialog = new AdDialog();
                adDialog.show(getFragmentManager(), "adDialog");
            }
        });

        return view;
    }
}
