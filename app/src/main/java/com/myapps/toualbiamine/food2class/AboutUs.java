package com.myapps.toualbiamine.food2class;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    public TextView team;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        TextView textview_aboutus = findViewById(R.id.abouttextview);
        textview_aboutus.setText("Food2Class is an online food ordering app which enables users to order food anywhere on campus");
        TextView team = findViewById(R.id.team);
        team.setText("Amine Toualbi \nAkhil Anand \nThomas Grant \nParker Hannifin \nJasmine Mixson \nBen Landry Salomon \nAbby Geddes \nSiddharth Mehta \nTrevor Graham");


    }

}
