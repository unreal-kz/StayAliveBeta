package com.example.unreal_kz.randomizer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class Randomizer extends AppCompatActivity implements View.OnClickListener {

    public String targetsHunterId, curUserId;
    private Button btnRandomize, btnBegin, btnSlay, btnNext;
    private EditText edtKillingId;
    private ParseQuery<ParseObject> parseObjectParseQuery, parseObjectParseQuerySecond, parseStayAliveAchievement;
    private ParseObject SAAvhievement;
    private int totalPlayerNumber;
    private String idToKill, usersCurId;
    private int killingsToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomizer);
        parseObjectParseQuery = ParseQuery.getQuery("StayAlive");
        parseObjectParseQuerySecond = ParseQuery.getQuery("StayAlive");
        parseStayAliveAchievement = ParseQuery.getQuery("StayAliveAchievement");
        try {
            usersCurId = ParseUser.getCurrentUser().getObjectId().toString().trim();
            totalPlayerNumber = ParseUser.getQuery().count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Toast.makeText(Randomizer.this, totalPlayerNumber + " |{ " + usersCurId + " }", Toast.LENGTH_LONG).show();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 2500);*/

        edtKillingId = (EditText) findViewById(R.id.edtKillingId);
        btnSlay = (Button) findViewById(R.id.btnSlay);
        btnBegin = (Button) findViewById(R.id.btnBegin);
        btnRandomize = (Button) findViewById(R.id.btnRandomize);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnBegin.setOnClickListener(this);
        btnRandomize.setOnClickListener(this);
        btnSlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        idToKill = edtKillingId.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btnSlay:
                Toast.makeText(Randomizer.this, "ID: " + idToKill, Toast.LENGTH_LONG).show();
                parseObjectParseQuery.getInBackground(idToKill, new GetCallback<ParseObject>() {
                    public ParseQuery parseObjectParseQueryThird = ParseQuery.getQuery("StayAlive");
                    ArrayList<String> myColection = new ArrayList<String>();

                    @Override
                    public void done(final ParseObject parseObject, ParseException exp) {
                        if (exp == null) {
                            targetsHunterId = parseObject.get("hunterId").toString().trim();
                            /*myColection.add(parseObject.get("userId").toString().trim());*/
                            Toast.makeText(Randomizer.this, "targetsHunterId:" + targetsHunterId, Toast.LENGTH_SHORT).show();
                            parseObjectParseQuerySecond.whereEqualTo("userId", usersCurId);
                            parseObjectParseQuerySecond.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if (e == null) {
                                        for (ParseObject myObj : list) {
                                            curUserId = myObj.getObjectId();
                                        }
                                        Toast.makeText(Randomizer.this, "curUserID:" + curUserId + " | " + targetsHunterId + " | " + usersCurId, Toast.LENGTH_LONG).show();
                                        if (targetsHunterId.equals(curUserId)) {
                                            Toast.makeText(Randomizer.this, "Right person to slay", Toast.LENGTH_SHORT).show();
                                            killingsToAdd = 0;
                                            parseStayAliveAchievement.whereEqualTo("userId", parseObject.get("userId").toString().trim());
                                            parseStayAliveAchievement.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> list, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject myParse : list) {
                                                            killingsToAdd = myParse.getNumber("killings").intValue();
                                                            myParse.put("isDead", true);
                                                            myParse.saveInBackground();
                                                        }
                                                    } else {

                                                    }
                                                }
                                            });
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    parseStayAliveAchievement.whereEqualTo("userId", usersCurId);
                                            /*myColection.add(usersCurId);*/
                                                    parseStayAliveAchievement.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> list, ParseException e) {
                                                            if (e == null) {
                                                                for (ParseObject myParse : list) {
                                                                    int toAdd = killingsToAdd + 1 + myParse.getNumber("killings").intValue();
                                                                    int moneyToAdd = myParse.getNumber("money").intValue() + 50;
                                                                    myParse.put("killings", toAdd);
                                                                    myParse.put("money", moneyToAdd);
                                                                    myParse.saveInBackground();
                                                                }
                                                                Toast.makeText(Randomizer.this, "1111", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(Randomizer.this, e.getMessage() + " 1111", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }, 2000);
                                            parseObjectParseQueryThird.whereEqualTo("hunterId", idToKill);
                                            parseObjectParseQueryThird.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> list, ParseException e) {
                                                    if (e == null) {
                                                        for (ParseObject myParse : list) {
                                                            myParse.put("hunterId", curUserId);
                                                            myParse.saveInBackground();
                                                        }
                                                    } else {
                                                        Toast.makeText(Randomizer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            ParseQuery<ParseObject> parseObjectParseQueryFourth = ParseQuery.getQuery("StayAlive");
                                            parseObjectParseQueryFourth.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> list, ParseException e) {
                                                    if (e == null) {
                                                        int tempCounter = 1;
                                                        for (ParseObject myParse : list) {
                                                            if (myParse.getObjectId().equals(idToKill)) {
                                                                myParse.deleteEventually();
                                                            } else {
                                                                myParse.put("p_counter", tempCounter);
                                                                tempCounter++;
                                                                myParse.saveEventually();
                                                            }
                                                        }
                                                        Toast.makeText(Randomizer.this, "New P_counters are SET", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                    }
                                                }
                                            });
                                            /*parseStayAliveAchievement.getInBackground(usersCurId, new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject parseObject, ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(Randomizer.this, "1111", Toast.LENGTH_SHORT).show();
                                                        parseObject.put("isDead", true);
                                                    } else {
                                                        Toast.makeText(Randomizer.this, e.getMessage() + " 1111", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });*/
                                                /*parseStayAliveAchievement.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId()).getFirst().put("killings",
                                                        parseStayAliveAchievement.whereEqualTo("hunterId", ParseUser.getCurrentUser().getObjectId()).getFirst().getNumber("killings").intValue() + 1);
                                                parseStayAliveAchievement.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId()).getFirst().saveInBackground();*/

                                            /*parseObject.saveInBackground();*/
                                        }
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Randomizer.this, exp.getMessage() + " 2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btnBegin:
                parseObjectParseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        int p_counter = 0;
                        if (e == null) {
                            for (ParseObject parseObject : list) {
                                p_counter = parseObject.getNumber("p_counter").intValue();
                                if (p_counter == 1) {
                                    try {
                                        parseObject.put("hunterId", parseObjectParseQuery.
                                                whereEqualTo("p_counter", totalPlayerNumber).
                                                getFirst().getObjectId());
                                        parseObject.saveInBackground();
                                        btnBegin.setVisibility(View.GONE);
                                        btnSlay.setVisibility(View.VISIBLE);
                                        edtKillingId.setVisibility(View.VISIBLE);
                                    } catch (ParseException exp) {
                                        Toast.makeText(Randomizer.this, exp.getMessage(), Toast.LENGTH_SHORT).show();
                                        exp.printStackTrace();
                                    }
                                } else {
                                    try {
                                        parseObject.put("hunterId", parseObjectParseQuery.
                                                whereEqualTo("p_counter", p_counter - 1).getFirst().getObjectId());
                                        parseObject.saveInBackground();
                                    } catch (ParseException exp) {
                                        Toast.makeText(Randomizer.this, exp.getMessage(), Toast.LENGTH_SHORT).show();
                                        exp.printStackTrace();
                                    }
                                }
                            }
                            Toast.makeText(Randomizer.this, "GOOD", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Randomizer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btnNext:
                btnBegin.setVisibility(View.GONE);
                btnSlay.setVisibility(View.VISIBLE);
                edtKillingId.setVisibility(View.VISIBLE);
                break;
            case R.id.btnRandomize:
                startActivity(new Intent(Randomizer.this,QRGenerator.class));
                break;
        }
    }
}
