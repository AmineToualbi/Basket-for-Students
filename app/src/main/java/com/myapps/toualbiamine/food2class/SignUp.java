package com.myapps.toualbiamine.food2class;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.myapps.toualbiamine.food2class.Model.User;
import com.myapps.toualbiamine.food2class.Utils.PasswordHasher;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.libsodium.jni.NaCl;

public class SignUp extends AppCompatActivity {

    EditText emailInput;
    EditText nameInput;
    EditText passwordInput;

    Button signUpBtn;

    ProgressBar signUpProgressBar;

    final String TAG = "SignUpActivity";

    FirebaseDatabase database;
    DatabaseReference tableUser;
    FirebaseAuth firebaseAuth;

    String signUpEmail;
    String fullSignUpEmail;
    String signUpPassword;
    String signUpName;

    PasswordHasher passwordhasher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignUp);
        nameInput = (MaterialEditText) findViewById(R.id.nameSignUp);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignUp);
        passwordhasher = new PasswordHasher();

        signUpBtn = (Button) findViewById(R.id.signUpBtn);

        signUpProgressBar = (ProgressBar) findViewById(R.id.signUpProgressBar);
        signUpProgressBar.setVisibility(View.INVISIBLE);

        //Initialize Firebase.
        database = FirebaseDatabase.getInstance();
        tableUser = database.getReference("Users");  //Get the table User created in the db.
        firebaseAuth = FirebaseAuth.getInstance();


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 signUpEmail = convertToFirebaseFormat(emailInput.getText().toString());
                 fullSignUpEmail = emailInput.getText().toString();
                 signUpName  = nameInput.getText().toString();
                 signUpPassword = passwordInput.getText().toString();
                 Log.i(TAG, "Sign Up Email = " + signUpEmail);

                 addUserToRealTimeDB();

            }
        });


    }

    private void signUpUserInAuth(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        signUpProgressBar.setVisibility(View.INVISIBLE);

                        if(task.isSuccessful()) {
                            Log.d(TAG, "Auth Sign Up : OK");

                       //     Toast.makeText(getApplicationContext(), "Verify your email to complete the creation of your account!", Toast.LENGTH_SHORT).show();

                            Toast.makeText(getApplicationContext(), "Account created. Check your email!", Toast.LENGTH_SHORT).show();

                            sendVerificationEmail();

                        }

                        else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Auth Sign Up : ERR " + task.getException().getMessage());

                        }
                    }

                });


    }

    private void sendVerificationEmail() {

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {

                            FirebaseAuth.getInstance().signOut();

                            Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goToMain);

                            finish();


                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getApplicationContext(),
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void addUserToRealTimeDB() {

        signUpProgressBar.setVisibility(View.VISIBLE);

        //Get data in the DB once only. addValueEventListener listens to every data change.
        tableUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Check if wrong email format.
                if(signUpEmail.equals("")) {
                    Toast.makeText(getApplicationContext(), "Wrong email format (firstlast@students.suu.edu)", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Toast 1");
                    signUpProgressBar.setVisibility(View.INVISIBLE);

                }

                //Check if user already exists.
                else if(dataSnapshot.child(signUpEmail).exists()) {
                    Toast.makeText(getApplicationContext(), "Account already exists with this email.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Toast 2");
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                }

                else if(signUpPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Toast 3");
                    signUpProgressBar.setVisibility(View.INVISIBLE);

                }

                else {

                    String encryptedPassword = passwordhasher.hashPassword(signUpPassword);

                    User newUser = new User(signUpEmail, signUpName, encryptedPassword);
                    tableUser.child(signUpEmail).setValue(newUser);

                    Log.d(TAG, "DB Sign Up : OK");

                    signUpUserInAuth(fullSignUpEmail, signUpPassword);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    /*    signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUpProgressBar.setVisibility(View.VISIBLE);

                //Get the data by querying the DB. This returns a snapshot of everything in the Table "User".
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

                            User newUser = new User(signUpEmail, signUpName, signUpPassword);
                            tableUser.child(signUpEmail).setValue(newUser);
                            Toast.makeText(getApplicationContext(), "Account created!", Toast.LENGTH_SHORT).show();

                            Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goToMain);

                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });*/



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

        return formatted.toLowerCase();

    }
}
