package com.github.skittlesdev.kubrick.utils;

import android.text.TextUtils;
import com.parse.ParseUser;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;

/**
 * Created by lowgr on 10/30/2015.
 */
public class ProfileElement {
    private String mAvatarUrl;
    private String mName;
    private String mEmail;

    public ProfileElement() {}

    public ProfileElement(ParseUser user) {
        setName(user.getUsername());
        setEmail(user.getEmail());
    }

    public String getAvatarUrl() {
        if (!TextUtils.isEmpty(getEmail())) {
            return new Gravatar().setSize(100).setDefaultImage(GravatarDefaultImage.IDENTICON).getUrl(getEmail());
        }
        else {
            return "";
        }
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
