package sample;

import javafx.scene.control.TextField;
import sample.buildings.sport_fac;

public class TextFieldNew extends TextField implements Control{
    public String type;

    public TextFieldNew(String prompt, String type) {
        super(prompt);
        this.type = type;
        //this listener is used for deleting ":", which can break deserialization after
        super.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.endsWith(":")) {
                super.setText(oldValue);
            }
        });
    }

    public TextFieldNew(int prompt, String type) {
        super(Integer.toString(prompt));
        this.type = type;
        super.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int x = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                super.setText(oldValue);
            }
        });
    }

    public TextFieldNew(float prompt, String type) {
        super(Float.toString(prompt));
        this.type = type;
        super.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                float x = Float.parseFloat(newValue);
            } catch (NumberFormatException e) {
                super.setText(oldValue);
            }
        });
    }

    @Override
    public String getVal() {
        return super.getText();
    }

    @Override
    public String getType() {

        return this.type;
    }

    @Override
    public Object getObj() {
        return null;
    }

    @Override
    public Class<? extends sport_fac> getCl() {
        return null;
    }

    @Override
    public void setObj(Object Obj) {

    }


    public void setType(String type) {
        this.type = type;
    }


}
