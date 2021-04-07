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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.PrincipalActivity;

import java.util.regex.Pattern;


public class LoginFragment extends Fragment {

    TextView registerNew;
    FloatingActionButton btnLogin;
    private TextInputLayout edt_Email, edt_Password;
    private FirebaseAuth Auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        registerNew = (TextView) root.findViewById(R.id.tv_registerNew);
        btnLogin = (FloatingActionButton) root.findViewById(R.id.btn_login_next);

        edt_Email = root.findViewById(R.id.edt_loginEmail);
        edt_Password = root.findViewById(R.id.edt_loginPassword);

        Auth = FirebaseAuth.getInstance();


        registerNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_Login_to_Register);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        return root;

    }

    private void refreshUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getContext(), PrincipalActivity.class));
        }
    }

    private boolean validateEmail(String email) {
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (email.isEmpty()) {
            edt_Email.setError(getText(R.string.emptyField));
            return false;
        } else if (!email.matches(checkEmail)) {
            edt_Email.setError(getText(R.string.incorrect_email));
            return false;
        } else {
            edt_Email.setError(null);
            edt_Email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String password = edt_Password.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            edt_Password.setError(getText(R.string.emptyField));
            return false;
        } else {
            edt_Password.setError(null);
            edt_Password.setErrorEnabled(false);
            return true;
        }
    }
    public void login(View view) {
        String email = edt_Email.getEditText().getText().toString();
        String password = edt_Password.getEditText().getText().toString();

        if (!validateEmail(email) | !validatePassword()) {
            return;
        }
        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = Auth.getCurrentUser();
                    refreshUI(user);
                } else {
                    Toast.makeText(view.getContext(), R.string.incorrect_username, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}