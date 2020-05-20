package net.awolf.businesstrade.model.response;

/**
 * Created by Work on 2017/8/5.
 */

public class UserLoginModel {
    public int AccountID;
    public String Username;
    public String Token;
    public String Expire;
    public String Redirect;

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getExpire() {
        return Expire;
    }

    public void setExpire(String expire) {
        Expire = expire;
    }

    public String getRedirect() {
        return Redirect;
    }

    public void setRedirect(String redirect) {
        Redirect = redirect;
    }
}
