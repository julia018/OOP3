package sample;

import javafx.scene.control.CheckBox;

public class CheckBoxNew extends CheckBox implements Control {
    public String type;

    public CheckBoxNew(String text, String type, boolean bool) {
        super(text);
        super.setSelected(bool);
        this.type = type;
    }


    @Override
    public Boolean getVal() {
        return Boolean.valueOf(super.isSelected());
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public Class getCl() {
        return null;
    }

    @Override
    public void setObj(Object Obj) {

    }
}
