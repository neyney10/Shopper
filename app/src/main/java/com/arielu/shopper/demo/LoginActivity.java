
package com.arielu.shopper.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity  {

    //// Private variables - UI Views. ////
    private EditText editTextEmail, editTextPassword;
    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_srceen);

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

    /**
     * Login the user using editTextEmail and editTextPassword to the service.
     * @param view  - the button clicked
     */
    public void LogInClick(View view)
    {
        // TODO
    }

    /**
     * Register the user using editTextEmail and editTextPassword to the service.
     * @param view
     */
    public void RegisterClick(View view)
    {
        // TODO
    }


}
