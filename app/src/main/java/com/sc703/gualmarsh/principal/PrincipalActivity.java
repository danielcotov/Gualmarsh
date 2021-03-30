package com.sc703.gualmarsh.principal;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sc703.gualmarsh.login.LoginActivity;
import com.sc703.gualmarsh.R;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        BottomNavigationView navView = findViewById(R.id.bottomNav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.dashboardFragment, R.id.itemsFragment, R.id.searchFragment, R.id.settingsFragment)
                .build();


        NavController navController = Navigation.findNavController(PrincipalActivity.this, R.id.nav_principal_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        /*Button btnSignOut = findViewById(R.id.btn_sign_out);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(PrincipalActivity.this, R.string.signed_out, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/
    }
}