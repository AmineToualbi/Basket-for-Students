package com.myapps.toualbiamine.food2class;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;
import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;

    Button btnSignIn;
    CheckBox rememberMeCb;

    ProgressBar signInProgressBar;

    final String TAG = "SignInActivity";

    FirebaseDatabase database;
    DatabaseReference tableUser;
    FirebaseAuth firebaseAuth;

    String signInEmail;
    String signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignIn);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignIn);

        btnSignIn = (Button) findViewById(R.id.signInBtn);
        rememberMeCb = (CheckBox) findViewById(R.id.rememberMeCb);

        signInProgressBar = (ProgressBar) findViewById(R.id.signInProgressBar);
        signInProgressBar.setVisibility(View.INVISIBLE);

        //Initialize Firebase.
        database = FirebaseDatabase.getInstance();
        tableUser = database.getReference("Users");   //Get the table User created in the db.
        firebaseAuth = FirebaseAuth.getInstance();

        //Initialize Paper => library to use to save key-value pairs on phone storage = easier than SharedPreferences.
        Paper.init(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInProgressBar.setVisibility(View.VISIBLE);

                loginUser();


            }
        });


    }

    private void getUserInfoFromDB() {


        //Get the data by querying the database.
        tableUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Data inputted by the user -> credentials.
                signInEmail = convertToFirebaseFormat(emailInput.getText().toString());
                signInPassword = passwordInput.getText().toString();


                //Check if email inputted is .edu & user exists in db.
                if(!(signInEmail.equals("")) && dataSnapshot.child(signInEmail).exists()) {

                    //Get User information.
                    User user = dataSnapshot.child(signInEmail).getValue(User.class);
                    user.setEmail(signInEmail);

                    signInProgressBar.setVisibility(View.INVISIBLE);

                    if (user.getPassword().equals(signInPassword)) {

                        if(rememberMeCb.isChecked() == true) {      //Save email & password to remember.
                            Paper.book().write(Common.USER_KEY, signInEmail);
                            Paper.book().write(Common.PWD_KEY, signInPassword);
                            Paper.book().write(Common.NAME_KEY, user.getName());
                        }

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


    private void loginUser() {

        signInEmail = emailInput.getText().toString();
        signInPassword = passwordInput.getText().toString();

        if(TextUtils.isEmpty(signInEmail) || TextUtils.isEmpty(signInPassword)) {
            Toast.makeText(getApplicationContext(), "Please, enter all the fields.", Toast.LENGTH_SHORT).show();
            signInProgressBar.setVisibility(View.INVISIBLE);
        }

        else {

            firebaseAuth.signInWithEmailAndPassword(signInEmail, signInPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.d(TAG, "FirebaseAuth : OK");

                    signInProgressBar.setVisibility(View.INVISIBLE);

                    if(!task.isSuccessful()) {
                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplicationContext(), "Your email and/or password may be incorrect!", Toast.LENGTH_SHORT).show();
                        }
                        else if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(getApplicationContext(), "No account found for the email address!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        checkIfEmailVerified();
                    }

                }
            });

        }

    }

    private void checkIfEmailVerified() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user.isEmailVerified() == true) {
            getUserInfoFromDB();
        }
        else {
            firebaseAuth.signOut();
            Toast.makeText(getApplicationContext(), "Verify your email!", Toast.LENGTH_SHORT).show();
        }

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
