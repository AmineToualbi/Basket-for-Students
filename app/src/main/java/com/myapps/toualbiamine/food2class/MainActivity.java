package com.myapps.toualbiamine.food2class;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.User;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button signInBtn;
    Button signUpBtn;
    TextView appSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = (Button) findViewById(R.id.btnSignIn);
        signUpBtn = (Button) findViewById(R.id.btnSignUp);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/restaurant_font.otf");
        //appSlogan.setTypeface(font);        //Change fonts of slogan in main screen.
        signInBtn.setTypeface(font);
        signUpBtn.setTypeface(font);

        //Initialize Paper => library to use to save key-value pairs on phone storage = easier than SharedPreferences.
        Paper.init(this);

        //Check if the user asked to be remembered.
        String rememberedEmail = Paper.book().read(Common.USER_KEY);
        String rememberedPassword = Paper.book().read(Common.PWD_KEY);
        String rememberedName = Paper.book().read(Common.NAME_KEY);

        if(rememberedEmail != null && rememberedPassword != null) {
            User rememberedUser = new User(rememberedEmail, rememberedName, rememberedPassword);
            Common.currentUser = rememberedUser;
            Intent goToHome = new Intent(getApplicationContext(), Home.class);
            startActivity(goToHome);
            finish();
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

}
