package com.sc703.gualmarsh.principal.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.sc703.gualmarsh.LocaleHelper;
import com.sc703.gualmarsh.MainActivity;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.login.LoginActivity;
import com.sc703.gualmarsh.principal.inventory.ItemViewModel;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        LinearLayout btn_logOut = root.findViewById(R.id.btn_settings_logOut);
        LinearLayout btn_contactSupport = root.findViewById(R.id.btn_settings_contactSupport);
        LinearLayout btn_callUs = root.findViewById(R.id.btn_settings_callUs);
        LinearLayout btn_aboutUs = root.findViewById(R.id.btn_settings_aboutUs);
        LinearLayout btn_translate = root.findViewById(R.id.btn_settings_translate);


        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS), 0);
            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), R.string.signed_out, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_settings_to_Web);

            }
        });
        btn_callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel: 800-8000-7222"));
                startActivity(intent);
            }
        });
        btn_contactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] TO = {"gadafasolutions@gmail.com"};
                String SUBJECT = "Support Assistance";
                String MAIL = "Hi Support Team," + "\n" + "I need your assistance with the app." + "\n" + "Kind regards,";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, TO);
                intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, MAIL);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        return root;
    }

}
