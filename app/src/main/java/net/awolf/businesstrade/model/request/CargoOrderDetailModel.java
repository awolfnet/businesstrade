package net.awolf.businesstrade.model.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaohai on 2017/9/23.
 */

public class CargoOrderDetailModel {
    public int cargoid;
    public int buyerid;
    public Date orderdate;
    public String comment;
    public BigDecimal amount;

    public int getCargoid() {
        return cargoid;
    }

    public void setCargoid(int cargoid) {
        this.cargoid = cargoid;
    }

    public int getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(int buyerid) {
        this.buyerid = buyerid;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
