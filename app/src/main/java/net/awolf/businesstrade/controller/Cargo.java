package net.awolf.businesstrade.controller;

import net.awolf.businesstrade.model.datatable.CargoFavoriteModel;
import net.awolf.businesstrade.model.datatable.CargoModel;

/**
 * Created by zhaohai on 2017/9/3.
 */

public class Cargo {

    private static Cargo mInstance=new Cargo();
    public static Cargo getInstance()
    {
        if(mInstance==null)
            mInstance=new Cargo();

        return mInstance;
    }

    private CargoModel cargo=null;
    private CargoFavoriteModel cargoFavorite=null;


    private String cargoDescription="";

    public String getCargoDescription() {
        return cargoDescription;
    }

    public void setCargoDescription(String cargoDescription) {
        this.cargoDescription = cargoDescription;
    }

    public CargoModel getCargo() {
        return cargo;
    }

    public void setCargo(CargoModel cargo) {
        this.cargo = cargo;
    }

    public CargoFavoriteModel getCargoFavorite() {
        return cargoFavorite;
    }

    public void setCargoFavorite(CargoFavoriteModel cargoFavorite) {
        this.cargoFavorite = cargoFavorite;
    }
}
