package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ChoiceBoxNew extends ChoiceBox implements Control{
    public String type;
    public ArrayList<Field> enumFields;


<<<<<<< HEAD
    public ChoiceBoxNew(ObservableList items, String type, Object o, ArrayList<Field> en_flds) {
=======
    public  ChoiceBoxNew(ObservableList items, String type, Object o, ArrayList<Field> en_flds) {
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
        super(items);
        if (o == null) {
            super.getSelectionModel().select(0);
        }
        else {
            super.getSelectionModel().select(o);
        }
        this.type = type;
        this.enumFields = en_flds;
    }


    @Override
    public String getVal() {
        String name = super.getSelectionModel().getSelectedItem().toString();
        String constName = "";
<<<<<<< HEAD
        for (Field fld : enumFields) {
            if (fld.getAnnotation(RusName.class).r_name().equals(name)) constName = fld.getName();
=======
        for(Field fld : enumFields){
            if(fld.getAnnotation(RusName.class).r_name().equals(name)) constName = fld.getName();
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
        }
        return constName;

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
    public Class getCl() {
        return null;
    }

    @Override
    public void setObj(Object Obj) {

    }

}
