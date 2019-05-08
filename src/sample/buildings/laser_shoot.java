package sample.buildings;

import sample.RusName;

@RusName(r_name = "Лазерный тир")
public class laser_shoot extends shoot_range {

    @RusName(r_name = "Тип проектора")
    private String project_type;

    @RusName(r_name = "Кол-во проекционых экранов")
    private int projscr_amount;

    public String getproject_type() {
        return project_type;
    }

    public void setproject_type(String project_type) {
        this.project_type = project_type;
    }

    public int getprojscr_amount() {
        return projscr_amount;
    }

    public void setprojscr_amount(String projscr_amount) {
        this.projscr_amount = Integer.parseInt(projscr_amount);
    }

    @Override
    public void deleteObject() {

    }
}


