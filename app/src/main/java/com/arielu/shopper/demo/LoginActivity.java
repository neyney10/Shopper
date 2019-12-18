
package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity  {

    //// Private variables - UI Views. ////
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin, btnRegister;

    //// Firebase authentication ////
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_srceen);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link UI views to private variables (such as buttons and stuff).
        UILink();
    }


    /**
     * Links UI views (elements) to object variables in the class.
     */
    private void UILink()
    {
        this.editTextEmail    = findViewById(R.id.email);
        this.editTextPassword = findViewById(R.id.password);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUserUI(currentUser);
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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUserUI(null);
                        }

                        // ...
                    }
                });
    }


    private void signInFirebase(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUserUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Firebase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUserUI(null);
                        }

                        // ...
                    }
                });
    }

    /**
     * Login the user using editTextEmail and editTextPassword to the service.
     * @param view  - the button clicked
     */
    public void LogInClick(View view)
    {
        signInFirebase(this.editTextEmail.getText().toString(),
                this.editTextPassword.getText().toString());
    }

    /**
     * Register the user using editTextEmail and editTextPassword to the service.
     * @param view
     */
    public void RegisterClick(View view)
    {
        // create a new account in firebase
        // if the account succesfully created then it will automatically sign in - then check if it indeed signed in.
        createNewAccountFirebase(this.editTextEmail.getText().toString(),
                         this.editTextPassword.getText().toString());

    }


}