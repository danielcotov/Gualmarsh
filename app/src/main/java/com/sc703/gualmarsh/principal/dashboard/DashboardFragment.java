package com.sc703.gualmarsh.principal.dashboard;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.login.LoginActivity;

public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if(Build.VERSION.SDK_INT >= 21){
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getActivity().getResources().getColor(R.color.w_darkBG));
        }

        /*Button btnSignOut = root.findViewById(R.id.btn_sign_out);
        Button btnWeb = root.findViewById(R.id.btn_web);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), R.string.signed_out, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_principal_fragment);
                navController.navigate(R.id.action_Dashboard_to_Web);
            }
        });*/


        return root;
    }
}