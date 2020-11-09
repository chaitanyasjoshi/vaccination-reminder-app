package com.mmcoe.pacem;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{
    private TextInputEditText mFullName, mMobile, mEmail, mPassword, mPasswordConfirm;
    private TextInputLayout mNameLayout, mMobileLayout, mEmailLayout, mPasswordLayout, mRePassLayout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private ProgressBar mProgress;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_register, container, false);
        mFullName = mView.findViewById(R.id.et_name);
        mMobile = mView.findViewById(R.id.et_mobile);
        mEmail = mView.findViewById(R.id.et_email);
        mPassword = mView.findViewById(R.id.et_password);
        mPasswordConfirm = mView.findViewById(R.id.et_repassword);
        mNameLayout = mView.findViewById(R.id.nameLayout);
        mMobileLayout = mView.findViewById(R.id.mobileLayout);
        mEmailLayout = mView.findViewById(R.id.email_Layout);
        mPasswordLayout = mView.findViewById(R.id.password_Layout);
        mRePassLayout = mView.findViewById(R.id.rePasswordLayout);
        Button mRegister = mView.findViewById(R.id.btn_register);

        mProgress = mView.findViewById(R.id.progressBarRegister);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mRegister.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View view) {
        mNameLayout.setError(null);
        mEmailLayout.setError(null);
        mMobileLayout.setError(null);
        mEmailLayout.setError(null);
        mPasswordLayout.setError(null);
        mRePassLayout.setError(null);
        validate();
    }

    private void validate (){
        final String email = Objects.requireNonNull(mEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(mPassword.getText()).toString().trim();
        String passwordConfirm = Objects.requireNonNull(mPasswordConfirm.getText()).toString().trim();
        final String fullName = Objects.requireNonNull(mFullName.getText()).toString();
        final String mobile = Objects.requireNonNull(mMobile.getText()).toString();
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

        if(TextUtils.isEmpty(passwordConfirm)){
            mRePassLayout.setError("Confirm your password");
            flag = 1;
        }

        if(TextUtils.isEmpty(fullName)){
            mNameLayout.setError("Full Name is required");
            flag = 1;
        }

        if(mobile.length()<10){
            mMobileLayout.setError("Enter a valid mobile");
            flag = 1;
        }

        if(flag == 1){
            return;
        }

        if(!password.equals(passwordConfirm)){
            Toast toast = Toast.makeText(getActivity(),"Passwords do not match", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        mProgress.setVisibility(View.VISIBLE);

        // Register the user through firebase
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"Successfully Registered", Toast.LENGTH_SHORT).show();

                    // Store user info in firebase firestore
                    DocumentReference documentReference = mStore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                    Map<String,Object> user = new HashMap<>();
                    user.put("fName",fullName);
                    user.put("email",email);
                    user.put("mobile",mobile);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user Profile created");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });

                    mProgress.setVisibility(View.INVISIBLE);
                    // Clear all the fields after successful registration
                    clearFields();
                }else {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void clearFields(){
        Objects.requireNonNull(mFullName.getText()).clear();
        Objects.requireNonNull(mMobile.getText()).clear();
        Objects.requireNonNull(mEmail.getText()).clear();
        Objects.requireNonNull(mPassword.getText()).clear();
        Objects.requireNonNull(mPasswordConfirm.getText()).clear();
    }
}
