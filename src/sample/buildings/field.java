package sample.buildings;

import sample.Composition;
import sample.RusName;

import java.io.Serializable;

@RusName(r_name = "Поле")
public class field implements Composition, Serializable {

    @RusName(r_name = "Газон")
    private grass gr_type;

    @RusName(r_name = "Ограждение")
    private Boolean fence;

    public String getgr_type() {
        return gr_type.toString();
    }

    public void setgr_type(String gr_t) {
        this.gr_type = grass.valueOf(gr_t);

    }

    public Boolean getfence() {
        return fence;
    }

    public void setfence(Boolean fence) {
        this.fence = fence;
    }

    private enum grass {
        @RusName(r_name = "Искусственный")
        synthetic,
        @RusName(r_name = "Натуральный")
        natural
    }


}
