package com.example.unreal_kz.randomizer.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unreal_kz.randomizer.MainActivity;
import com.example.unreal_kz.randomizer.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnSignUp;
    private EditText edtName, edtPass;
    private ParseUser parseUser;
    private ParseQuery<ParseUser> parseUserParseQuery = ParseUser.getQuery();
    private String username, password;
    private int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        try {
            counter = parseUserParseQuery.count()+1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parseUser = new ParseUser();

        edtName = (EditText) findViewById(R.id.edtUserName);
        edtPass = (EditText) findViewById(R.id.edtPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        username = edtName.getText().toString().trim();
        password = edtPass.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btnLogin:
                parseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e == null) {
                            Toast.makeText(Registration.this,"You have Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this, MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(Registration.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.btnSignUp:
                if(!username.isEmpty() && !password.isEmpty()){
                parseUser.setUsername(username);
                parseUser.setPassword(password);
                parseUser.put("p_counter", counter);
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(Registration.this, "You have Singed Up", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Registration.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }else{
                    Toast.makeText(Registration.this,"Enter login and passwrod", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
