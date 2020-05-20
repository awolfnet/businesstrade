package net.awolf.businesstrade.model.datatable;

/**
 * Created by Work on 2017/8/18.
 */

public class ProductModel {
    public int productid;
    public String name;
    public int markettypeid;

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarkettypeid() {
        return markettypeid;
    }

    public void setMarkettypeid(int markettypeid) {
        this.markettypeid = markettypeid;
    }
}
