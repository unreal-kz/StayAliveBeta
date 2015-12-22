package com.example.unreal_kz.randomizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unreal_kz.randomizer.registration.Registration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ParseUser parseUser;
    private TextView textViewUser;
    private ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
    private Button btnLogOut, btnStart, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {
            startActivity(new Intent(MainActivity.this, Registration.class));
            finish();
        } else {
            Toast.makeText(MainActivity.this, "Wellcome " + parseUser.getUsername(), Toast.LENGTH_SHORT).show();
            textViewUser = (TextView) findViewById(R.id.txtUserName);
            btnLogOut = (Button) findViewById(R.id.btnLogOut);
            btnStart = (Button) findViewById(R.id.btnStart);
            btnNext = (Button) findViewById(R.id.btnToBegin);
            textViewUser.setText("Hello, " + parseUser.getUsername().toUpperCase());

            btnNext.setOnClickListener(this);
            btnStart.setOnClickListener(this);
            btnLogOut.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnToBegin:
                startActivity(new Intent(MainActivity.this, Randomizer.class));
                break;
            case R.id.btnLogOut:
                parseUser.logOut();
                startActivity(new Intent(MainActivity.this, Registration.class));
                finish();
                break;
            case R.id.btnStart:
                parseUserParseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (e == null) {
                            for (ParseUser objUser : list) {
                                ParseObject stayAliveObj = new ParseObject("StayAlive");
                                ParseObject SAAvhievement = new ParseObject("StayAliveAchievement");
                                stayAliveObj.put("userId", objUser.getObjectId());
                                stayAliveObj.put("p_counter", objUser.get("p_counter"));
                                SAAvhievement.put("userId", objUser.getObjectId());
                                SAAvhievement.put("p_counter", objUser.get("p_counter"));
                                SAAvhievement.put("username", objUser.get("username"));
                                SAAvhievement.put("isDead", false);
                                SAAvhievement.put("killings", 0);
                                SAAvhievement.put("money", 100);
                                stayAliveObj.saveEventually();
                                SAAvhievement.saveInBackground();
                            }
                            Toast.makeText(MainActivity.this, "User info transferred", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //parseUser.logOut();
                startActivity(new Intent(MainActivity.this, Randomizer.class));
                //finish();
                break;
        }
    }
}
