package com.myapps.toualbiamine.food2class;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.User;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button signInBtn;
    Button signUpBtn;
    TextView appSlogan;

    String rememberedEmail;
    String rememberedPassword;
    String rememberedName;



    FirebaseDatabase database;
    DatabaseReference tableUser;

    boolean isStaff = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        signInBtn = (Button) findViewById(R.id.btnSignIn);
        signUpBtn = (Button) findViewById(R.id.btnSignUp);
        appSlogan = (TextView) findViewById(R.id.slogan);

        database = FirebaseDatabase.getInstance();
        tableUser = database.getReference("Users");   //Get the table User created in the db.
//
        //for testing purposes

        //Typeface font = getResources().getFont(R.font.nabila);
      //  appSlogan.setTypeface(font);        //Change font of slogan in main screen.

        //Initialize Paper => library to use to save key-value pairs on phone storage = easier than SharedPreferences.
        Paper.init(this);

        //Check if the user asked to be remembered.
        this.rememberedEmail = Paper.book().read(Common.USER_KEY);
        this.rememberedPassword = Paper.book().read(Common.PWD_KEY);
        this.rememberedName = Paper.book().read(Common.NAME_KEY);

        if(this.rememberedEmail != null && this.rememberedPassword != null) {

            getUserFlagCountFromDB();

        }



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToSignUp = new Intent(getApplicationContext(), SignUp.class);
                startActivity(goToSignUp);

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToSignIn = new Intent(getApplicationContext(), SignIn.class);
                startActivity(goToSignIn);

            }
        });



    }

    private void getUserFlagCountFromDB() {


        tableUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               int flagCount;
                User user = dataSnapshot.child(rememberedEmail).getValue(User.class);
                flagCount = user.getFlagCount();

                if (flagCount < 3){
                    User rememberedUser = new User(rememberedEmail, rememberedName, rememberedPassword, isStaff, flagCount);
                    Common.currentUser = rememberedUser;
                    Intent goToHome = new Intent(getApplicationContext(), Home.class);
                    startActivity(goToHome);
                    finish();
                }

                else{
                    Toast.makeText(getApplicationContext(), "Sorry your account is blocked. Please contact customer service", Toast.LENGTH_LONG).show();
                    Paper.book().destroy();

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
