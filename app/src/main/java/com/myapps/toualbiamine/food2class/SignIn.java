package com.myapps.toualbiamine.food2class;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;

    Button btnSignIn;

    ProgressBar signInProgressBar;

    final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignIn);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignIn);

        btnSignIn = (Button) findViewById(R.id.signInBtn);

        signInProgressBar = (ProgressBar) findViewById(R.id.signInProgressBar);
        signInProgressBar.setVisibility(View.INVISIBLE);

        //Initialize Firebase.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tableUser = database.getReference("Users");   //Get the table User created in the db.

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInProgressBar.setVisibility(View.VISIBLE);

                //Get the data by querying the database.
                tableUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Data inputted by the user -> credentials.
                        String signInEmail = convertToFirebaseFormat(emailInput.getText().toString());
                        String signInPassword = passwordInput.getText().toString();

                        signInProgressBar.setVisibility(View.INVISIBLE);

                        //Check if email inputted is .edu & user exists in db.
                        if(!(signInEmail.equals("")) && dataSnapshot.child(signInEmail).exists()) {

                            //Get User information.
                            User user = dataSnapshot.child(signInEmail).getValue(User.class);
                            user.setEmail(signInEmail);

                            if (user.getPassword().equals(signInPassword)) {

                                Intent goToHome = new Intent(getApplicationContext(), Home.class);
                                Common.currentUser = user;      //Let the app know that the current user is the that we just signed in.
                                startActivity(goToHome);
                                finish();

                            } else {

                                Toast.makeText(getApplicationContext(), "Your email and/or password may be incorrect!", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else {

                            Toast.makeText(getApplicationContext(), "No account found for the email address!", Toast.LENGTH_SHORT).show();

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
                break;
            }
        }

        if(!(domain.equals("students.suu.edu"))) {
            return "";
        }

        return formatted;

    }
}
