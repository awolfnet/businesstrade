package net.awolf.businesstrade.controller;

import net.awolf.businesstrade.util.Const;
import net.awolf.businesstrade.util.Utils;


/**
 * Created by Work on 2017/8/5.
 */

public class UserInfo {

    private static final String TAG="UerInfo";

    private static UserInfo mInstance=new UserInfo();

    private String userName;
    private String userToken;
    private int userID;

    public static synchronized UserInfo getInstance()
    {
        if(mInstance==null)
            mInstance=new UserInfo();

        return mInstance;
    }

    public void save()
    {
        Utils.saveSetting(Const.PHONENUM,this.getUserName());
        Utils.saveSetting(Const.TOKEN,UserInfo.this.getUserToken());
        Utils.saveSetting(Const.USERID,this.userID);
    }

    public void load()
    {
        this.setUserName(Utils.readSetting(Const.PHONENUM));
        this.setUserToken(Utils.readSetting(Const.TOKEN));
        this.setUserID(Utils.readSettingInt(Const.USERID));
    }

    public void logoff()
    {
        Utils.saveSetting(Const.PHONENUM,"");
        Utils.saveSetting(Const.TOKEN,"");
        Utils.saveSetting(Const.USERID,0);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
