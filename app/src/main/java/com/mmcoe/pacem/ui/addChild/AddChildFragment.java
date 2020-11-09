package com.mmcoe.pacem.ui.addChild;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mmcoe.pacem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.mmcoe.pacem.AppNotification.CHANNEL_ID;

public class AddChildFragment extends Fragment implements View.OnClickListener {
    private NotificationManagerCompat notificationManager;
    private TextInputEditText mName, mDob, mBloodGrp, mHeight, mWeight;
    private TextInputLayout mNameLayout, mDobLayout, mBloodGrpLayout, mHeightLayout, mWeightLayout;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    private long days;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addchild, container, false);

        // Initialization
        notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(getActivity()));
        mName = root.findViewById(R.id.et_name);
        mDob = root.findViewById(R.id.et_dob);
        mBloodGrp = root.findViewById(R.id.et_bloodGroup);
        mHeight = root.findViewById(R.id.et_height);
        mWeight = root.findViewById(R.id.et_weight);
        mNameLayout = root.findViewById(R.id.name_Layout);
        mDobLayout = root.findViewById(R.id.dobLayout);
        mBloodGrpLayout = root.findViewById(R.id.bloodGrpLayout);
        mHeightLayout = root.findViewById(R.id.heightLayout);
        mWeightLayout = root.findViewById(R.id.weightLayout);
        MaterialButton mAdd = root.findViewById(R.id.btn_add);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        // perform click event on edit text
        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                mDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mAdd.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        mNameLayout.setError(null);
        mDobLayout.setError(null);
        mBloodGrpLayout.setError(null);
        mHeightLayout.setError(null);
        mWeightLayout.setError(null);
        validate();
    }

    private void validate() {
        final String name = Objects.requireNonNull(mName.getText()).toString();
        final String dob = Objects.requireNonNull(mDob.getText()).toString();
        final String bloodGrp = Objects.requireNonNull(mBloodGrp.getText()).toString();
        final String height = Objects.requireNonNull(mHeight.getText()).toString();
        final String weight = Objects.requireNonNull(mWeight.getText()).toString();
        int flag = 0;

        if (TextUtils.isEmpty(name)) {
            mNameLayout.setError("Name is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(dob)) {
            mDobLayout.setError("Date of birth is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(bloodGrp)) {
            mBloodGrpLayout.setError("Blood group is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(height)) {
            mHeightLayout.setError("Height is required");
            flag = 1;
        }

        if (TextUtils.isEmpty(weight)) {
            mWeightLayout.setError("Weight is required");
            flag = 1;
        }

        if (flag == 1) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        Date strDate = null;
        try {
            strDate = sdf.parse(dob);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().before(strDate)) {
            mDobLayout.setError("Invalid date of birth");
            return;
        }

        Toast.makeText(getActivity(), "Child added successfully", Toast.LENGTH_SHORT).show();
        String[] strDOB = dob.split("/");
        LocalDate now = LocalDate.now();
        LocalDate birthDate = LocalDate.of(Integer.parseInt(strDOB[2]), Integer.parseInt(strDOB[1]), Integer.parseInt(strDOB[0]));

        days = ChronoUnit.DAYS.between(birthDate, now);

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref", MODE_PRIVATE);
        sharedPreferences.edit().putInt("age", (int) days).apply();
        checkVaccine();

        // Store user info in firebase firestore
        DocumentReference documentReference = mStore.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("child").document(name);
        Map<String, Object> child = new HashMap<>();
        child.put("dob", dob);
        child.put("bloodGrp", bloodGrp);
        child.put("height", height);
        child.put("weight", weight);
        documentReference.set(child).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: child's Profile created");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

        // Clear all the fields after successful registration
        clearFields();
    }

    private void checkVaccine(){
        if (days == 0 || days == 42 || days == 70 || days == 98 || days == 183 || days == 274 || days == 335 || days == 365 || days == 456 || days == 517 || days == 548 || days == 730 || days == 1460){
            notifyUser();
        }
    }

    private void notifyUser(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getActivity()), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentTitle("Reminder")
                .setContentText("Your child's vaccination is due tomorrow")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        notificationManager.notify(1,builder.build());
    }

    private void clearFields() {
        Objects.requireNonNull(mName.getText()).clear();
        Objects.requireNonNull(mDob.getText()).clear();
        Objects.requireNonNull(mBloodGrp.getText()).clear();
        Objects.requireNonNull(mHeight.getText()).clear();
        Objects.requireNonNull(mWeight.getText()).clear();
    }
}