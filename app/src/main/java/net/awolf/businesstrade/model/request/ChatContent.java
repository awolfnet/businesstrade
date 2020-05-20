package net.awolf.businesstrade.model.request;

/**
 * Created by zhaohai on 2017/10/21.
 */

public class ChatContent {
    public int userid;
    public String content;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
