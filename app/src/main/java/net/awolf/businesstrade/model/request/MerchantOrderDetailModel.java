package net.awolf.businesstrade.model.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhaohai on 2017/9/23.
 */

public class MerchantOrderDetailModel {
    public int merchantid;
    public int buyerid;
    public Date orderdate;
    public String orderlist;
    public String comment;
    public BigDecimal amount;
    public int amounttype;
}
