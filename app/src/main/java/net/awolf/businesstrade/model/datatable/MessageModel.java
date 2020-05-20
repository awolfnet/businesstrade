package net.awolf.businesstrade.model.datatable;

/**
 * Created by zhaohai on 2017/10/20.
 */

public class MessageModel {
    public int mid ;
    public int aid ;
    public int fromid ;
    public String Title ;
    public int Type ;
    public int Style ;
    public String Content ;
    public int Status ;
    private String timestamps;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getFromid() {
        return fromid;
    }

    public void setFromid(int fromid) {
        this.fromid = fromid;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getStyle() {
        return Style;
    }

    public void setStyle(int style) {
        Style = style;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getTimestamps() {
        return timestamps.replace('T',' ');
    }

    public void setTimestamps(String timestamps) {
        this.timestamps = timestamps;
    }
}
