package com.shleen.quixotic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private TextView txt_points_count;
    private TextView txt_word_count;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize typefaces
        Typeface BUTLER_REG = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Regular.ttf");
        Typeface BUTLER_BOLD = Typeface.createFromAsset(getContext().getAssets(), "fonts/Butler_Bold.ttf");

        // Get signed in user
        GoogleSignInAccount user = GoogleSignIn.getLastSignedInAccount(view.getContext());

        // Set img_user
        ImageView img_user = (ImageView) view.findViewById(R.id.img_user);
        String img_user_url = user.getPhotoUrl().toString();

        Glide.with(this).load(img_user_url).into(img_user);

        MaterialButton btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setTypeface(BUTLER_REG);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOutDialog signOutDialog = new SignOutDialog();
                signOutDialog.show(getFragmentManager(), "signOutDialog");
            }
        });

        ImageView img_ad = view.findViewById(R.id.img_ad);
        img_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdDialog adDialog = new AdDialog();
                adDialog.show(getFragmentManager(), "adDialog");
            }
        });

        // Set txt_name
        TextView txt_name = view.findViewById(R.id.txt_name);
        txt_name.setTypeface(BUTLER_BOLD);
        txt_name.setText(GoogleSignIn.getLastSignedInAccount(getContext()).getDisplayName());

        // Set txt_words & txt_points
        txt_word_count = view.findViewById(R.id.txt_word_count);
        txt_word_count.setTypeface(BUTLER_REG);
        database.getReference(String.format("%s/words", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")))
                    .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                txt_word_count.setText(Long.toString(dataSnapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // TODO: Handle database error
                            }
                }
        );

        txt_points_count = view.findViewById(R.id.txt_points_count);
        txt_points_count.setTypeface(BUTLER_REG);
        database.getReference(String.format("%s/points", user.getEmail().replaceAll("[^a-zA-Z0-9]", "")))
                .addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           txt_points_count.setText(dataSnapshot.getValue().toString());
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           // TODO: Handle database error
                       }
                   }
                );

        TextView txt_word_label = view.findViewById(R.id.txt_word_label);
        txt_word_label.setTypeface(BUTLER_REG);

        TextView txt_points_label = view.findViewById(R.id.txt_points_label);
        txt_points_label.setTypeface(BUTLER_REG);

        return view;
    }
}
