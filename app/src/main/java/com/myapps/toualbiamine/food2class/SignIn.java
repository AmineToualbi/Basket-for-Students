package com.myapps.toualbiamine.food2class;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.*;
import com.myapps.toualbiamine.food2class.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText emailInput;
    EditText passwordInput;
    Button btnSignIn;
    final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailInput = (MaterialEditText) findViewById(R.id.emailSignIn);
        passwordInput = (MaterialEditText) findViewById(R.id.passwordSignIn);
        btnSignIn = (Button) findViewById(R.id.signInBtn);

        //Initialize Firebase.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tableUser = database.getReference("User");   //Get the table User created in the db.

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setMessage("Logging in...");
                progressDialog.show();

                tableUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String signInEmail = convertToFirebaseFormat(emailInput.getText().toString());
                        String signInPassword = passwordInput.getText().toString();

                        progressDialog.dismiss();

                        //Check if email inputted is .edu & user exists in db.
                        if(!(signInEmail.equals("")) && dataSnapshot.child(signInEmail).exists()) {

                            //Get User information.
                            User user = dataSnapshot.child(signInEmail).getValue(User.class);
                            if (user.getPassword().equals(signInPassword)) {
                                Toast.makeText(getApplicationContext(), "Signed In!", Toast.LENGTH_SHORT).show();
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

    private String convertToFirebaseFormat(String email) {

        String formatted = "";
        String domain = "";

        //Faster to check from the end that going through the entire string.
        for(int i=email.length()-1; i>=0; i--) {
            char c = email.charAt(i);
            if(c == '.') {
                formatted = email.substring(0, i);
                domain = email.substring(i+1, email.length());
                Log.i(TAG, "Domain = " + domain);
                break;
            }
        }

        if(!(domain.equals("edu"))) {
            return "";
        }

        return formatted;

    }
}
