package net.awolf.businesstrade.model.request;

/**
 * Created by zhaohai on 2017/10/30.
 */

public class AccountRegisterInfo {
    public String account_name ;
    public String account_password ;
    public String account_mail ;
    public String account_nickname ;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_password() {
        return account_password;
    }

    public void setAccount_password(String account_password) {
        this.account_password = account_password;
    }

    public String getAccount_mail() {
        return account_mail;
    }

    public void setAccount_mail(String account_mail) {
        this.account_mail = account_mail;
    }

    public String getAccount_nickname() {
        return account_nickname;
    }

    public void setAccount_nickname(String account_nickname) {
        this.account_nickname = account_nickname;
    }
}
