package com.arielu.shopper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText email,password,confirmPassword,phoneNumber,name;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    public void init(){
        //spinner
        spinner = findViewById(R.id.user_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.spinner_items,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //EditText
        email = findViewById(R.id.email_field);
        password = findViewById(R.id.password_field);
        confirmPassword = findViewById(R.id.confirm_password_field);
        phoneNumber = findViewById(R.id.phone_number_field);
        name = findViewById(R.id.name_field);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
    private void updateUserUI(FirebaseUser currentUser) {
        // if not connected then "currentUser" is equal to null
        if(currentUser != null)
        { // signed in.
            // Switch to user's panel activity.
            Intent intent = new Intent(this, UserPanelActivity.class);
            startActivity(intent);
        }
        else
        { // not signed in.
            // TODO
        }

    }

    private void createNewAccountFirebase(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUserUI(null);
                        }

                        // ...
                    }
                });
    }

    public void Register(View view){
                createNewAccountFirebase(this.email.getText().toString(),
                        this.password.getText().toString());
        Toast toast = Toast.makeText(getApplicationContext(), "you have been registered", Toast.LENGTH_SHORT);
        toast.show();
    }
}
