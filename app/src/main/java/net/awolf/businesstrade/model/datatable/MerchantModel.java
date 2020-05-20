package net.awolf.businesstrade.model.datatable;

/**
 * Created by Work on 2017/8/18.
 */

public class MerchantModel {
    public int merchantid ;
    public String merchant_name ;
    public String merchant_contact ;
    public String merchant_website ;
    public String merchant_description ;
    public int merchant_owner_id ;

    public int getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(int merchantid) {
        this.merchantid = merchantid;
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
}
