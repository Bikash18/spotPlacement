package com.smartPark.spotPlacement.model;

public class LoginCheck {

    private Boolean isLogin;

    public LoginCheck(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean isLogin) {
        isLogin = isLogin;
    }
}
