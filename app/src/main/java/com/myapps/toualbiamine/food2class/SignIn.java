package com.myapps.toualbiamine.food2class;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import org.libsodium.jni.NaCl;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;

    Button btnSignIn;
    CheckBox rememberMeCb;
    TextView forgotPassword;

    ProgressBar signInProgressBar;

    final String TAG = "SignInActivity";

    FirebaseDatabase database;
    DatabaseReference tableUser;
    FirebaseAuth firebaseAuth;

    String signInEmail;
    String signInPassword;

    Dialog forgotPasswordPopup;
    Button resetPassword;
    ImageButton closePopupBtn;
    EditText forgotPwdEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignIn);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignIn);

        btnSignIn = (Button) findViewById(R.id.signInBtn);
        rememberMeCb = (CheckBox) findViewById(R.id.rememberMeCb);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);

        signInProgressBar = (ProgressBar) findViewById(R.id.signInProgressBar);
        signInProgressBar.setVisibility(View.INVISIBLE);

        forgotPasswordPopup = new Dialog(this);

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

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordPopup();
            }
        });


    }

    private void showForgotPasswordPopup() {

        //Show the popup.
        forgotPasswordPopup.setContentView(R.layout.popup_forgot_password);
        forgotPasswordPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forgotPasswordPopup.show();

        resetPassword = (Button) forgotPasswordPopup.findViewById(R.id.resetBtn);
        closePopupBtn = (ImageButton) forgotPasswordPopup.findViewById(R.id.closePopup);
        forgotPwdEmail = (MaterialEditText) forgotPasswordPopup.findViewById(R.id.forgotEmailAddress);

        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordPopup.dismiss();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resetEmailAddress = forgotPwdEmail.getText().toString();

                if(TextUtils.isEmpty(resetEmailAddress)) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(resetEmailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Email sent!", Toast.LENGTH_SHORT).show();
                                        Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(goToMain);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Failed to send reset password email.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }

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

                 //   if (user.getPassword().equals(signInPassword)) {

                        if(rememberMeCb.isChecked() == true) {      //Save email & password to remember.
                            Paper.book().write(Common.USER_KEY, signInEmail);
                            Paper.book().write(Common.PWD_KEY, signInPassword);
                            Paper.book().write(Common.NAME_KEY, user.getName());
                        }


                        //if(!(user.getPassword().equals(signInPassword))) {
                        if(!checkPassword(signInPassword,user.getPassword())){
                            String encryptedPassword = hashPassword(signInPassword);
                            user.setPassword(encryptedPassword);
                            tableUser.child(signInEmail).setValue(user);
                        }

                        Intent goToHome = new Intent(getApplicationContext(), Home.class);
                        Common.currentUser = user;      //Let the app know that the current user is the that we just signed in.
                        startActivity(goToHome);
                        finish();

                    } /*else {

                        Toast.makeText(getApplicationContext(), "Your email and/or password may be incorrect!", Toast.LENGTH_SHORT).show();

                    }

                }
                else {

                    Toast.makeText(getApplicationContext(), "No account found for the email address!", Toast.LENGTH_SHORT).show();

                }*/

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
    //checks password and returns false if the password is incorrect
    public boolean checkPassword(String guess,String theHashedPassword){
        byte[] passBytes = guess.getBytes();
        byte[] hashedPass = stringToByteArray(theHashedPassword);
        if(NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_str_verify(hashedPass,passBytes,passBytes.length)!=0){
            //wrong password
            return false;
        }
        //right password
        return true;
    }
    private String hashPassword(String password){
        byte[] hashedPass = new byte[NaCl.sodium().crypto_pwhash_strbytes()];
        byte[] passBytes = password.getBytes();
        NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_str(hashedPass,passBytes,passBytes.length,NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_opslimit_interactive(),NaCl.sodium().crypto_pwhash_scryptsalsa208sha256_memlimit_interactive());
        return byteArrayToString(hashedPass);
    }
    //converts a byte array into a special string that can be undone by stringToByteArray
    private String byteArrayToString(byte[] a){
        String s = "";
        for(byte c:a){
            s+=c+",";
        }
        return s;
    }
    //converts a string made from byteArrayToString back into a byte array
    private byte[] stringToByteArray(String encryptedPassword){
        byte[] hashedPass = new byte[NaCl.sodium().crypto_pwhash_strbytes()];
        String[] splitString = encryptedPassword.split(",");
        if(splitString.length == hashedPass.length){
            for(int i=0;i<splitString.length;i++) {
                hashedPass[i]=Byte.parseByte(splitString[i]);
            }

        }
        else{
            System.out.println("Why aren't they the same size?!");
        }
        return hashedPass;
    }
}
