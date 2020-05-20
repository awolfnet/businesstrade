package net.awolf.businesstrade.model.datatable;

import java.math.BigDecimal;

/**
 * Created by zhaohai on 2017/10/27.
 */

public class CargoOrderHistoryModel {
    public int id;
    public String order_id;
    public String order_date;
    public String order_comment;
    public BigDecimal order_amount;
    public String cargo_plate;
    public String cargo_driver;
    public String cargo_contact;
    public int cargo_owner_id;
    public int cargo_id;
    public int buyer_id;
    public String buyer_contact;
    public String buyer_name;

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

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

    public String getCargo_plate() {
        return cargo_plate;
    }

    public void setCargo_plate(String cargo_plate) {
        this.cargo_plate = cargo_plate;
    }

    public String getCargo_driver() {
        return cargo_driver;
    }

    public void setCargo_driver(String cargo_driver) {
        this.cargo_driver = cargo_driver;
    }

    public String getCargo_contact() {
        return cargo_contact;
    }

    public void setCargo_contact(String cargo_contact) {
        this.cargo_contact = cargo_contact;
    }

    public int getCargo_owner_id() {
        return cargo_owner_id;
    }

    public void setCargo_owner_id(int cargo_owner_id) {
        this.cargo_owner_id = cargo_owner_id;
    }

    public int getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(int cargo_id) {
        this.cargo_id = cargo_id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getBuyer_contact() {
        return buyer_contact;
    }

    public void setBuyer_contact(String buyer_contact) {
        this.buyer_contact = buyer_contact;
    }
}
