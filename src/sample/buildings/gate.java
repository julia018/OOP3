package sample.buildings;

import sample.Composition;
import sample.RusName;

import java.io.Serializable;

@RusName(r_name = "Ворота")
public class gate implements Composition, Serializable {

    @RusName(r_name = "Высота")
    private float height;

    @RusName(r_name = "Ширина")
    private float width;

    @RusName(r_name = "Подвижность")
    private Boolean mobility;

    public float getheight() {
        return height;
    }

    public void setheight(String height) {
        this.height = Float.parseFloat(height);
    }

    public float getwidth() {
        return width;
    }

    public void setwidth(String width) {
        this.width = Float.parseFloat(width);
    }

    public Boolean getmobility() {
        return mobility;
    }

    public void setmobility(Boolean mobility) {
        this.mobility = mobility;
    }
}
