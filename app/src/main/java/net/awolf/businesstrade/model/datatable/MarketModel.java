package net.awolf.businesstrade.model.datatable;

/**
 * Created by Work on 2017/8/18.
 */

public class MarketModel {
    private int market_id;
    private String market_name;
    private String market_location ;
    private String market_typename;

    public int getMarket_id() {
        return market_id;
    }

    public void setMarket_id(int market_id) {
        this.market_id = market_id;
    }

    public String getMarket_name() {
        return market_name;
    }

    public void setMarket_name(String market_name) {
        this.market_name = market_name;
    }

    public String getMarket_location() {
        return market_location;
    }

    public void setMarket_location(String market_location) {
        this.market_location = market_location;
    }

    public String getMarket_typename() {
        return market_typename;
    }

    public void setMarket_typename(String market_typename) {
        this.market_typename = market_typename;
    }
}
