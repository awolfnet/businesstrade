package net.awolf.businesstrade.controller;

/**
 * Created by Work on 2017/8/12.
 */

public class Market {

    private static Market mInstance=new Market();
    public static Market getInstance()
    {
        if(mInstance==null)
            mInstance=new Market();

        return mInstance;
    }

    private String location="";
    private String marketType="";
    private String marketName="";
    private String productName="";

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getMarketType() {
        return marketType;
    }
    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getMarketName() {
        return marketName;
    }
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

}
