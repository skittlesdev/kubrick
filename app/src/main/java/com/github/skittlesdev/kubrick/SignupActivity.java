package com.github.skittlesdev.kubrick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ((Button) findViewById(R.id.signup)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        String password = ((TextView) findViewById(R.id.password)).getText().toString();
        String email = ((TextView) findViewById(R.id.email)).getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(KubrickApplication.getContext(), ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(KubrickApplication.getContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}