package com.sc703.gualmarsh.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.dashboard.DashboardActivity;


public class LoginFragment extends Fragment {

    TextView registerNew;
    FloatingActionButton btnLogInGo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_login, container, false);

        registerNew = (TextView) root.findViewById(R.id.tv_registerNew);
        btnLogInGo = (FloatingActionButton) root.findViewById(R.id.btn_logInGo);

        registerNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_Login_to_Register);
            }
        });

        btnLogInGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DashboardActivity.class));
            }
        });
        return root;

    }
}