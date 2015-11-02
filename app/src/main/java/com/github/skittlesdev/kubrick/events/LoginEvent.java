package com.github.skittlesdev.kubrick.events;

import com.parse.ParseUser;

public class LoginEvent {
    private ParseUser user;

    public LoginEvent(ParseUser user) {
        this.user = user;
    }

    public ParseUser getUser() {
        return user;
    }
}
