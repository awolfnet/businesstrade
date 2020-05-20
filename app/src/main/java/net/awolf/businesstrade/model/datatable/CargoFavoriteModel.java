package net.awolf.businesstrade.model.datatable;

/**
 * Created by zhaohai on 2017/9/4.
 */

public class CargoFavoriteModel {
    private int favorite_id;
    private int cargo_id;
    private String cargo_plate;
    private String cargo_driver;
    private String driver_licence ;
    private String driver_contact;
    private String cargo_description;
    private int cargo_owner_id;
    private int aid;

    public int getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(int favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(int cargo_id) {
        this.cargo_id = cargo_id;
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

    public String getDriver_licence() {
        return driver_licence;
    }

    public void setDriver_licence(String driver_licence) {
        this.driver_licence = driver_licence;
    }

    public String getDriver_contact() {
        return driver_contact;
    }

    public void setDriver_contact(String driver_contact) {
        this.driver_contact = driver_contact;
    }

    public String getCargo_description() {
        return cargo_description;
    }

    public void setCargo_description(String cargo_description) {
        this.cargo_description = cargo_description;
    }

    public int getCargo_owner_id() {
        return cargo_owner_id;
    }

    public void setCargo_owner_id(int cargo_owner_id) {
        this.cargo_owner_id = cargo_owner_id;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
}
