package net.awolf.businesstrade.controller;

import net.awolf.businesstrade.model.datatable.MerchantFavoriteModel;
import net.awolf.businesstrade.model.datatable.MerchantModel;

/**
 * Created by zhaohai on 2017/9/10.
 */

public class Merchant {

    private static Merchant mInstance=new Merchant();
    public static Merchant GetInstance()
    {
        if(mInstance==null)
            mInstance=new Merchant();
        return mInstance;
    }

//    private MarketModel market=null;
    private MerchantModel merchant=null;
    private MerchantFavoriteModel merchantFavorite=null;


//    public MarketModel getMarket() {
//        return market;
//    }
//    public void setMarket(MarketModel market) {
//        this.market = market;
//    }


    public MerchantModel getMerchant() {
        return merchant;
    }
    public void setMerchant(MerchantModel merchant) {
        this.merchant = merchant;
    }

    public MerchantFavoriteModel getMerchantFavorite() {
        return merchantFavorite;
    }
    public void setMerchantFavorite(MerchantFavoriteModel merchantFavorite) {
        this.merchantFavorite = merchantFavorite;
    }
}
