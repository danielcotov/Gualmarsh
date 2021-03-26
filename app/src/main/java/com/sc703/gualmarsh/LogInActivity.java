package com.sc703.gualmarsh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sc703.gualmarsh.dashboard.DashboardActivity;

public class LogInActivity extends Activity {

    TextView registerNew;
    FloatingActionButton btnLogInGo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        registerNew = (TextView) findViewById(R.id.tv_registerNew);
        btnLogInGo = (FloatingActionButton) findViewById(R.id.btn_logInGo);

        registerNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
            }
        });

        btnLogInGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, DashboardActivity.class));
            }
        });
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        registerNew = (TextView) root.findViewById(R.id.tv_registerNew);

        registerNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft =
            }
        });

        return root;

    }*/
}