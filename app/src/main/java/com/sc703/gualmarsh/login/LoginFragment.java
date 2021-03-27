package com.sc703.gualmarsh.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sc703.gualmarsh.LoginActivity;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.dashboard.DashboardActivity;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

    TextView registerNew;
    FloatingActionButton btnLogInGo;
    private EditText edt_Email, edt_Password, edt_confirmPassword;
    private FirebaseAuth Auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_login, container, false);

        registerNew = (TextView) root.findViewById(R.id.tv_registerNew);
        btnLogInGo = (FloatingActionButton) root.findViewById(R.id.btn_logInGo);
        registerNew = (TextView) root.findViewById(R.id.tv_registerNew);
        btnLogInGo = (FloatingActionButton) root.findViewById(R.id.btn_logInGo);

        edt_Email = root.findViewById(R.id.edt_logInEmail);
        edt_Password = root.findViewById(R.id.edt_logInPassword);

        Auth = FirebaseAuth.getInstance();


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
                login(v);
//                startActivity(new Intent(getContext(), DashboardActivity.class));
            }
        });

        return root;

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = Auth.getCurrentUser();
        refreshUI(currentUser);
    }
    private void refreshUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getContext(), DashboardActivity.class));
        }
    }
    private boolean emailValidation(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public void login(View view) {
        String email = edt_Email.getText().toString();
        String password = edt_Password.getText().toString();
//        Toast.makeText(<T>, email, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(),email,Toast.LENGTH_SHORT).show();
        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener((LoginActivity)getContext(), new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (emailValidation(email)) {
                    if (!TextUtils.isEmpty(password) && password.length() >= 6) {
                        if (task.isSuccessful()) {
                            //Obtenemos el usuario que se esta autenticando
                            FirebaseUser user = Auth.getCurrentUser();
                            refreshUI(user);
                        } else {
                            Toast.makeText(view.getContext(), "The username you enter is incorrect, please try again", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "The password is incorrect, password is 6 characters minimum", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "The email format is incorrect", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}