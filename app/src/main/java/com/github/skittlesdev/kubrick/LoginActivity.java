package com.github.skittlesdev.kubrick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.github.skittlesdev.kubrick.events.LoginEvent;
import com.parse.*;
import com.vlonjatg.progressactivity.ProgressActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((Button) findViewById(R.id.login)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ((ProgressActivity) findViewById(R.id.progressActivity)).showLoading();

        String username = ((TextView) findViewById(R.id.username)).getText().toString();
        String password = ((TextView) findViewById(R.id.password)).getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    KubrickApplication.getEventBus().post(new LoginEvent(parseUser));
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("user", parseUser);
                    installation.saveInBackground();
                    finish();
                }
                else {
                    ((ProgressActivity) findViewById(R.id.progressActivity)).showContent();
                    Toast.makeText(KubrickApplication.getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
