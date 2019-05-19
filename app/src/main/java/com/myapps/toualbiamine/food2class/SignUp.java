package com.myapps.toualbiamine.food2class;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.myapps.toualbiamine.food2class.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    EditText emailInput;
    EditText nameInput;
    EditText passwordInput;
    Button signUpBtn;
    ProgressBar signUpProgressBar;
    final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignUp);
        nameInput = (MaterialEditText) findViewById(R.id.nameSignUp);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignUp);
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
        signUpProgressBar = (ProgressBar) findViewById(R.id.signUpProgressBar);
        signUpProgressBar.setVisibility(View.INVISIBLE);


        //Initialize Firebase.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tableUser = database.getReference("User");  //Get the table User created in the db.

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpProgressBar.setVisibility(View.VISIBLE);

                tableUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String signUpEmail = convertToFirebaseFormat(emailInput.getText().toString());
                        String signUpName  = nameInput.getText().toString();
                        String signUpPassword = passwordInput.getText().toString();
                        Log.i(TAG, "Sign Up Email = " + signUpEmail);

                        signUpProgressBar.setVisibility(View.INVISIBLE);

                        //Check if wrong email format.
                        if(signUpEmail.equals("")) {
                            Toast.makeText(getApplicationContext(), "Wrong email format (firstlast@students.suu.edu)", Toast.LENGTH_SHORT).show();

                        }
                        //Check if user already exists.
                        else if(dataSnapshot.child(signUpEmail).exists()) {
                            Toast.makeText(getApplicationContext(), "Account already exists with this email.", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            User newUser = new User(signUpName, signUpPassword);
                            tableUser.child(signUpEmail).setValue(newUser);
                            Toast.makeText(getApplicationContext(), "Account created!", Toast.LENGTH_SHORT).show();
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });


    }


    //SUU uses weird format for email -> mohamedtoualbi@students.suu.edu.
     private String convertToFirebaseFormat(String email) {

        String formatted = "";
        String domain = "";

        //Faster to check from the end that going through the entire string.
        for(int i=0; i<email.length(); i++) {
            char c = email.charAt(i);
            if(c == '@') {
                formatted = email.substring(0, i);
                domain = email.substring(i+1, email.length());
                Log.i(TAG, "Domain = " + domain);
                break;
            }
        }

        if(!(domain.equals("students.suu.edu"))) {
            return "";
        }

        return formatted;

    }
}
