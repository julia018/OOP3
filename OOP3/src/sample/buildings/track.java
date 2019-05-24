package sample.buildings;

import sample.Composition;
import sample.RusName;

import java.io.Serializable;

@RusName(r_name = "Сектор трека")
public class track implements Composition, Serializable {


    public void setamount(String am) {
        this.amount = track_am.valueOf(am);
    }

    public String getamount() {
        return amount.toString();
    }

    public void settr_length(String len) {
        this.tr_length = Float.parseFloat(len);
    }

    private enum track_am {
        @RusName(r_name = "менее 2-ух")
        less2,
        @RusName(r_name = "от 2-ух до 5-ти")
        two_five,
        @RusName(r_name = "более 5-ти")
        greater_five,
    }

    @RusName(r_name = "Количество дорожек")
    private track_am amount;

    public float gettr_length() {
        return tr_length;
    }

    @RusName(r_name = "Протяженность дорожки")
    private float tr_length;

}
