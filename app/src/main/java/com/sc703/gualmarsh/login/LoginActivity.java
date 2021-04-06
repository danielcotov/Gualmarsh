package com.sc703.gualmarsh.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
        requestPermissions();
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
    private void requestPermissions() {
        final int PERMISSION_CODE = 55895;
        int GPS = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int phone = ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int internet = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        if (GPS != PackageManager.PERMISSION_GRANTED
                || phone != PackageManager.PERMISSION_GRANTED
                || internet != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET}, PERMISSION_CODE);
            }

        }
    }
}

