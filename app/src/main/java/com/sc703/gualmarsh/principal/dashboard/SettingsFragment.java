package com.sc703.gualmarsh.principal.dashboard;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.login.LoginActivity;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        SolicitarPermisos();
        Button btnSignOut = root.findViewById(R.id.btn_logOut);
        Button btnWeb = root.findViewById(R.id.btn_Web);
        Button btnPhone = root.findViewById(R.id.btn_Phone);

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
                Navigation.findNavController(v).navigate(R.id.action_settings_to_Web);

            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: 800-8000-722  "));
                startActivity(intent);
            }
        });


        return root;
    }
    private void SolicitarPermisos(){
        final int PERMISSION_CODE = 55895;
        int Permiso_GPS = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int Permiso_Telefono = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
        int Permiso_Internet = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);

        if(Permiso_GPS != PackageManager.PERMISSION_GRANTED
                || Permiso_Telefono != PackageManager.PERMISSION_GRANTED
                || Permiso_Internet != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET},PERMISSION_CODE);
            }
        }
    }
}
