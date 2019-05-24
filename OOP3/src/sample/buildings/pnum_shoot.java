package sample.buildings;

import sample.RusName;

@RusName(r_name = "Пневманический тир")
public class pnum_shoot extends shoot_range {

    @RusName(r_name = "Винтовки")
    private String gun_type;

    @RusName(r_name = "Отдача при выстреле")
    private Boolean recoil;


    public String getgun_type() {
        return gun_type;
    }


    public Boolean getrecoil() {
        return recoil;
    }

    public void setrecoil(Boolean recoil) {
        this.recoil = recoil;
    }

    public void setgun_type(String gun_type) {
        this.gun_type = gun_type;
    }

    @Override
    public void deleteObject() {

    }
}
