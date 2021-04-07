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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sc703.gualmarsh.R;
import com.sc703.gualmarsh.principal.PrincipalActivity;

import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    TextView alreadyRegistered;
    FloatingActionButton btnRegister;
    private TextInputLayout edt_Email, edt_Password, edt_confirmPassword, edt_Name;
    private FirebaseAuth Auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        alreadyRegistered = (TextView) root.findViewById(R.id.tv_alreadyRegistered);
        btnRegister = (FloatingActionButton) root.findViewById(R.id.btn_register_next);
        edt_Name = root.findViewById(R.id.edt_registerName);
        edt_Email = root.findViewById(R.id.edt_registerEmail);
        edt_Password = root.findViewById(R.id.edt_registerPassword);
        edt_confirmPassword = root.findViewById(R.id.edt_registerConfirmPassword);
        Auth = FirebaseAuth.getInstance();


        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_Register_to_Login);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(v);
//                startActivity(new Intent(getContext(), DashboardActivity.class));
            }
        });
        return root;
    }

    private void refreshUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getContext(), PrincipalActivity.class));
        }
    }

    private Boolean validateName() {
        String name = edt_Name.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            edt_Name.setError(getText(R.string.emptyField));
            return false;
        } else {
            edt_Name.setError(null);
            edt_Name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {
        String email = edt_Email.getEditText().getText().toString().trim();
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
        String checkPassword = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[!@#$%^&+=])" +    //at least 1 special character
                ".{1,}" +
                "$";

        String checkPasswordLength = "^" +
                ".{6,}" +               //at least 4 characters
                "$";

        if (password.isEmpty()) {
            edt_Password.setError(getText(R.string.emptyField));
            return false;
        } else if (!password.matches(checkPassword)) {
            edt_Password.setError(getText(R.string.invalid_password));
            return false;
        } else if (!password.matches(checkPasswordLength)) {
            edt_Password.setError(getText(R.string.invalid_password_length));
            return false;
        } else {
            edt_Password.setError(null);
            edt_Password.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String confirmPassword = edt_confirmPassword.getEditText().getText().toString().trim();
        String password = edt_Password.getEditText().getText().toString().trim();
        System.out.println(password);
        System.out.printf(confirmPassword);
        if (confirmPassword.isEmpty()) {
            edt_confirmPassword.setError(getText(R.string.emptyField));
            return false;
        } else if (!confirmPassword.equals(password)) {
            edt_confirmPassword.setError(getText(R.string.password_mismatch));
            return false;
        } else {
            edt_confirmPassword.setError(null);
            edt_confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void registration(View view) {
        String email = edt_Email.getEditText().getText().toString();
        String password = edt_Password.getEditText().getText().toString().trim();

        System.out.println(email);
        System.out.printf(password);
        if (!validateName() | !validateEmail() | !validatePassword() | !validateConfirmPassword()) {
            return;
        }

        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = Auth.getCurrentUser();
                    refreshUI(user);
                } else {
                    Toast.makeText(view.getContext(), R.string.registration_failed, Toast.LENGTH_LONG).show();
                }
            }


        });

    }
}