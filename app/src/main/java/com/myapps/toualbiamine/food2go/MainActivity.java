package com.myapps.toualbiamine.food2go;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        appSlogan = (TextView) findViewById(R.id.slogan);

        Typeface font = getResources().getFont(R.font.nabila);
        appSlogan.setTypeface(font);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}
