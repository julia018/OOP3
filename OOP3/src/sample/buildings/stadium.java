package sample.buildings;

import sample.RusName;

public abstract class stadium extends sport_fac {

    @RusName(r_name = "Наличие VIP-зоны")
    private Boolean vip;

    @RusName(r_name = "Вместительность парковки")
    private int parking;

    public Boolean getvip() {
        return vip;
    }

    public void setvip(Boolean vip) {
        this.vip = vip;
    }


    public int getparking() {
        return parking;
    }

    public void setparking(String parking) {
        this.parking = Integer.parseInt(parking);
    }


}
