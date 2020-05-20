package net.awolf.businesstrade.model.datatable;

/**
 * Created by Work on 2017/8/22.
 */

public class MerchantFavoriteModel {
    private int favorite_id;
    private int merchant_id;
    private String merchant_name;
    private String merchant_contact;
    private String merchant_website;
    private String merchant_description;
    private int merchant_owner_id;
    private int aid;

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_contact() {
        return merchant_contact;
    }

    public void setMerchant_contact(String merchant_contact) {
        this.merchant_contact = merchant_contact;
    }

    public String getMerchant_website() {
        return merchant_website;
    }

    public void setMerchant_website(String merchant_website) {
        this.merchant_website = merchant_website;
    }

    public String getMerchant_description() {
        return merchant_description;
    }

    public void setMerchant_description(String merchant_description) {
        this.merchant_description = merchant_description;
    }

    public int getMerchant_owner_id() {
        return merchant_owner_id;
    }

    public void setMerchant_owner_id(int merchant_owner_id) {
        this.merchant_owner_id = merchant_owner_id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
}
