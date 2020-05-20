package net.awolf.businesstrade.model.datatable;

/**
 * Created by Work on 2017/8/18.
 */

public class AdModel {
    public int adid;
    public String imgsrc;
    public String imglink;
    public String imgalt;
    public int pagelevel;

    public int getAdid() {
        return adid;
    }

    public void setAdid(int adid) {
        this.adid = adid;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getImgalt() {
        return imgalt;
    }

    public void setImgalt(String imgalt) {
        this.imgalt = imgalt;
    }

    public int getPagelevel() {
        return pagelevel;
    }

    public void setPagelevel(int pagelevel) {
        this.pagelevel = pagelevel;
    }
}
