package com.mmcoe.pacem;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout mEmailLayout, mPasswordLayout;
    private TextInputEditText mEmail, mPassword;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_login, container, false);
        MaterialButton mLogin = mView.findViewById(R.id.btn_login);
        mEmailLayout = mView.findViewById(R.id.emailLayout);
        mPasswordLayout = mView.findViewById(R.id.passwordLayout);
        mEmail = mView.findViewById(R.id.et_email);
        mPassword = mView.findViewById(R.id.et_password);

        /*mEmailLayout.setVisibility(View.INVISIBLE);
        mPasswordLayout.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);
        mPassword.setVisibility(View.INVISIBLE);
        tvTitle.setVisibility(View.INVISIBLE);
        swipe.setVisibility(View.INVISIBLE);
        mLogin.setVisibility(View.INVISIBLE);*/

        // Check if the user is already logged in
        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getActivity(),DashboardActivity.class);
            startActivity(intent);
            // Make Login fragment non-returnable
            Objects.requireNonNull(getActivity()).finish();
        } else {*/
            // No user is signed in
            mProgressBar = mView.findViewById(R.id.progressBarLogin);
            mAuth = FirebaseAuth.getInstance();

            /*mEmailLayout.setVisibility(View.VISIBLE);
            mPasswordLayout.setVisibility(View.VISIBLE);
            mEmail.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.VISIBLE);
            mLogin.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            swipe.setVisibility(View.VISIBLE);*/

            // Set on click listener for login button
            mLogin.setOnClickListener(this);
        //}
        return mView;
    }

    @Override
    public void onClick(View view) {
        // Clear previous error messages before validation
        mEmailLayout.setError(null);
        mPasswordLayout.setError(null);
        validate();
    }

    private void validate(){
        String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
        int flag = 0;

        //  Perform validations on edit text fields
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailLayout.setError("Enter a valid email id");
            flag = 1;
        }

        if(TextUtils.isEmpty(password)){
            mPasswordLayout.setError("Password is required");
            flag = 1;
        }

        if(flag == 1){
            return;
        }

        // Make progress bar visible
        mProgressBar.setVisibility(View.VISIBLE);

        // Validate login through firebase
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // TODO: Start date change service here
                    // Start Dashboard activity
                    Intent intent = new Intent(getActivity(),DashboardActivity.class);
                    startActivity(intent);
                    mProgressBar.setVisibility(View.INVISIBLE);

                    // Make Login fragment non-returnable
                    Objects.requireNonNull(getActivity()).finish();
                }else {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
