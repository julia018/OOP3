package sample.buildings;

import sample.RusName;

@RusName(r_name = "Теннисный корт")
public class tennis_cort extends sport_fac {

    public Boolean getchecker() {
        return checker;
    }

    public void setchecker(Boolean dividers) {
        this.checker = dividers;
    }

    @RusName(r_name = "Датчик касания сетки")
    private Boolean checker;

    @Override
    public void deleteObject() {

    }

    private enum covering {
        @RusName(r_name = "Хард")
        hard,
        @RusName(r_name = "Грунт")
        grount,
        @RusName(r_name = "Трава")
        grass,
        @RusName(r_name = "Паркет")
        parket
    }

    public String getcover() {
        return cover.toString();
    }

    public void setcover(String cov) {
        this.cover = covering.valueOf(cov);
    }

    @RusName(r_name = "Покрытие")
    private covering cover;

}
