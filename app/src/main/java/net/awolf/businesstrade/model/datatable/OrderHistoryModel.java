package net.awolf.businesstrade.model.datatable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaohai on 2017/10/12.
 */

public class OrderHistoryModel {
    public int id;
    public  String order_id ;
    public String order_date ;
    public  String order_list ;
    public  String order_comment ;
    public BigDecimal order_amount ;
    public  String merchant_name ;
    public  String merchant_contact ;
    public int merchant_id ;
    public String merchant_owner_id;
    public  String buyer_contact ;
    public int buyer_id ;
    public String buyer_name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date.replace('T',' ');
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_list() {
        return order_list;
    }

    public void setOrder_list(String order_list) {
        this.order_list = order_list;
    }

    public String getOrder_comment() {
        return order_comment;
    }

    public void setOrder_comment(String order_comment) {
        this.order_comment = order_comment;
    }

    public BigDecimal getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(BigDecimal order_amount) {
        this.order_amount = order_amount;
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

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getBuyer_contact() {
        return buyer_contact;
    }

    public void setBuyer_contact(String buyer_contact) {
        this.buyer_contact = buyer_contact;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getMerchant_owner_id() {
        return merchant_owner_id;
    }

    public void setMerchant_owner_id(String merchant_owner_id) {
        this.merchant_owner_id = merchant_owner_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }
}
