package sample.buildings;

import sample.RusName;

import java.io.Serializable;

public abstract class sport_fac implements Serializable {

    @RusName(r_name = "Расположение")
    private String location;

    @RusName(r_name = "Вместимость")
    private int capacity;

    @RusName(r_name = "Кол-во этажей")
    private int fl_amount;

    private enum light {
        @RusName(r_name = "Искусственное")
        synthetic,
        @RusName(r_name = "Естественное")
        natural
    }

    @RusName(r_name = "Освещение")
    private light light_type;

    @RusName(r_name = "Название")
    private String name;

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }

    public int getcapacity() {
        return capacity;
    }

    public void setcapacity(String capacity) {
        this.capacity = Integer.parseInt(capacity);
    }

    public int getfl_amount() {
        return fl_amount;
    }

    public void setfl_amount(String fl_amount) {
        this.fl_amount = Integer.parseInt(fl_amount);
    }

    public String getlight_type() {
        return this.light_type.toString();
    }

    public void setlight_type(String l_t) {
        this.light_type = light.valueOf(l_t);
    }

    public abstract void deleteObject();
}
