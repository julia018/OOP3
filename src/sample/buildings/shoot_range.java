package sample.buildings;

import sample.RusName;

public abstract class shoot_range extends sport_fac{

    @RusName(r_name = "Количество стрелковых позиций")
    private int rpos_amount;

    @RusName(r_name = "Наличие учебных мишеней")
    private Boolean learn_targets;

    public int getrpos_amount() {
        return rpos_amount;
    }


    public void setrpos_amount(String rpos_amount) {
        this.rpos_amount = Integer.parseInt(rpos_amount);
    }

    public Boolean getlearn_targets() {
        return this.learn_targets;
    }

    public void setlearn_targets(Boolean learn_targets) {
        this.learn_targets = learn_targets;
    }


}
