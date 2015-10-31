package com.github.skittlesdev.kubrick.utils;

/**
 * Created by lowgr on 10/30/2015.
 */
public class ProfileElement {
    private int mAvatar;
    private String mName;
    private String mEmail;

    public ProfileElement(int avatar, String name, String email) {
        this.mAvatar = avatar;
        this.mName = name;
        this.mEmail = email;
    }

    public void setAvatar(int avatar) {
        this.mAvatar = avatar;
    }

    public int getAvatar() {
        return this.mAvatar;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getEmail() {
        return this.mEmail;
    }
}
