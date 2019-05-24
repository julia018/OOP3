package sample;

import javafx.scene.control.Label;
import sample.buildings.sport_fac;

public class LabelNew extends Label implements Control {
    private Class cl;
    private Object obj;
    private String type;

    public Object getObj() {
        return obj;
    }

    @Override
    public void setObj(Object obj) {
        cl.cast(obj);
        this.obj = obj;
    }


    public Class<? extends sport_fac> getCl() {
        return cl;
    }


    public void setCl(Class cl) {
        this.cl = cl;
    }


    public LabelNew(String text, Class cl, String t) {
        super(text);
        this.cl = cl;
        this.obj = null;
        this.type = t;
    }

    @Override
    public Object getVal() {
        return this.obj;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
