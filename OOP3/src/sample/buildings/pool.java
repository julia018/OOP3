package sample.buildings;

import sample.RusName;

@RusName(r_name = "Бассейн")
public class pool extends sport_fac {

    @RusName(r_name = "Способ обеззараживания воды")
    private desinfection des;

    @RusName(r_name = "Разделители дорожек")
    private Boolean dividers;

    public Boolean getdividers() {
        return dividers;
    }

    @RusName(r_name = "Разделители дорожек")
    public void setdividers(Boolean dividers) {
        this.dividers = dividers;
    }

    @Override
    public void deleteObject() {

    }

    private enum desinfection {
        @RusName(r_name = "Хлором")
        chlor,
        @RusName(r_name = "Озоном")
        ozon,
        @RusName(r_name = "Натрием")
        natrium,
        @RusName(r_name = "Ультразвуком")
        ultravoice
    }

    public String getdes() {
        return des.toString();
    }

    public void setdes(String des) {
        this.des = desinfection.valueOf(des);
    }


}
