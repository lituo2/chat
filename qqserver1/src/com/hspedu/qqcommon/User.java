package com.hspedu.qqcommon;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String userwd;

    public User(String userId, String userwd) {
        this.userId = userId;
        this.userwd = userwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserwd() {
        return userwd;
    }

    public void setUserwd(String userwd) {
        this.userwd = userwd;
    }
}
