package com.myapps.toualbiamine.food2class;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    public TextView teamNames;
    public TextView description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        description = findViewById(R.id.aboutUsDescription);
        description.setText("Food2Class is an online food ordering app which enables users to order food anywhere on campus");
        teamNames = findViewById(R.id.aboutUsTeamNames);
        teamNames.setText("Amine Toualbi \nAkhil Anand \nThomas Grant \nParker Hannifin \nJasmine Mixson \nBen Landry Salomon \nAbby Geddes \nSiddharth Mehta \nTrevor Graham");

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/restaurant_font.otf");
        description.setTypeface(font);
        teamNames.setTypeface(font);
    }

}
