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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.sc703.gualmarsh.LocaleHelper;
import com.sc703.gualmarsh.MainActivity;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.login.LoginActivity;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    boolean languageSelected = true;
    Resources resources;
    Context context;
    //Settings
    TextView tv_settings_changeLanguage, tv_settings_aboutUs, tv_settings_callUs,tv_settings_contactSupport,
            tv_settings_logOut, tv_settings_main;

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

        //Settings
        tv_settings_changeLanguage = root.findViewById(R.id.tv_settings_changeLanguage);
        tv_settings_aboutUs = root.findViewById(R.id.tv_settings_aboutUs);
        tv_settings_callUs = root.findViewById(R.id.tv_settings_callUs);
        tv_settings_contactSupport = root.findViewById(R.id.tv_settings_contactSupport);
        tv_settings_logOut = root.findViewById(R.id.tv_settings_logOut);
        tv_settings_main = root.findViewById(R.id.tv_settingsMain);


        btn_translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] language ={"ENGLISH","ESPAÑOL"};
                final int checkedItem;

                if(languageSelected){
                    checkedItem = 0;
                }else{
                    checkedItem = 1;
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Select language").setSingleChoiceItems(language, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        languageSelected = language[which].equals("ENGLISH");

                        if(language[which].equals("ENGLISH")){
                            context = LocaleHelper.setLocale(getContext(), "en");
                            resources = context.getResources();

                            //Settings
                            tv_settings_changeLanguage.setText(resources.getString(R.string.settings_translate));
                            tv_settings_aboutUs.setText(resources.getString(R.string.settings_Web));
                            tv_settings_callUs.setText(resources.getString(R.string.settings_phone));
                            tv_settings_logOut.setText(resources.getString(R.string.settings_Log_Out));
                            tv_settings_contactSupport.setText(resources.getString(R.string.settings_email));
                            tv_settings_main.setText(resources.getString(R.string.settings_main));

                        }
                        if(language[which].equals("ESPAÑOL")){
                            context = LocaleHelper.setLocale(getContext(), "es");
                            resources = context.getResources();

                            //Settings
                            tv_settings_changeLanguage.setText(resources.getString(R.string.settings_translate));
                            tv_settings_aboutUs.setText(resources.getString(R.string.settings_Web));
                            tv_settings_callUs.setText(resources.getString(R.string.settings_phone));
                            tv_settings_logOut.setText(resources.getString(R.string.settings_Log_Out));
                            tv_settings_contactSupport.setText(resources.getString(R.string.settings_email));
                            tv_settings_main.setText(resources.getString(R.string.settings_main));

                        }
                    }

                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
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
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, "gadafasolutions@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "Insert email body");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });


        return root;
    }

}
