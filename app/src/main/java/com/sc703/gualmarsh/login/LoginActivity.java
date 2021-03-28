package com.sc703.gualmarsh.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.PrincipalActivity;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth Auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            FirebaseUser currentUser = Auth.getCurrentUser();
            refreshUI(currentUser);
        }catch(Exception e){
            e.printStackTrace();

        }
        //getSupportFragmentManager().beginTransaction().add(R.id.fragmentHolder, new HomeLogin()).commit();
        }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = Auth.getCurrentUser();
        refreshUI(currentUser);
    }
    private void refreshUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, PrincipalActivity.class));
        }
    }

    }

