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

public class RegisterFragment extends Fragment {

    TextView alreadyRegistered;
    FloatingActionButton btnLogInGo;
    private EditText edt_Email, edt_Password, edt_confirmPassword;
    private FirebaseAuth Auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        alreadyRegistered = (TextView) root.findViewById(R.id.tv_alreadyRegistered);
        btnLogInGo = (FloatingActionButton) root.findViewById(R.id.btn_logInGo);
        btnLogInGo = (FloatingActionButton) root.findViewById(R.id.btn_logInGo);


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

        btnLogInGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration(v);
//                startActivity(new Intent(getContext(), DashboardActivity.class));
            }
        });
        return root;
    }
    private boolean emailValidation(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    public boolean passValidation(final String password) {

        final Pattern Password_Pat = Pattern.compile("^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$");
        if (password.isEmpty()) {
            return false;
        } else if (!Password_Pat.matcher(password).matches()) {
            return false;
        } else {
            return true;
        }

    }
    private void refreshUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(getContext(), DashboardActivity.class));
        }
    }
    public void registration(View view) {
        String email = edt_Email.getText().toString();
        String password = edt_Password.getText().toString().trim();
        String confirmPassword = edt_confirmPassword.getText().toString().trim();

        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((LoginActivity) getContext(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (emailValidation(email)) {
                    if (!TextUtils.isEmpty(password) && !passValidation(password) && password.length() >= 6 && password != confirmPassword) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = Auth.getCurrentUser();
                            refreshUI(user);
                        } else {
                            Toast.makeText(view.getContext(), "The creation of the user failed", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "The password is not valid", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "The email format is not correct", Toast.LENGTH_LONG).show();
                }
            }

        });

    }
}