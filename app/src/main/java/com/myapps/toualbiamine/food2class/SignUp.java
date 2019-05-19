package com.myapps.toualbiamine.food2class;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    EditText emailInput;
    EditText nameInput;
    EditText passwordInput;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignUp);
        nameInput = (MaterialEditText) findViewById(R.id.nameSignUp);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignUp);
        signUp = (Button) findViewById(R.id.signUpBtn);



    }
}
