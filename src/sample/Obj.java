package sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import sample.buildings.sport_fac;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

<<<<<<< HEAD
public class Obj implements Serializable {

    Object object;
    Class<?> cl_name;
    private String name;
    private String class_name;
=======
public class Obj {
    Object object;
    Class cl_name;
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101

    public Class getCl_name() {
        return cl_name;
    }

    public void setCl_name(Class cl_name) {
        this.cl_name = cl_name;
    }

<<<<<<< HEAD
=======
    private transient SimpleStringProperty name;

>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

<<<<<<< HEAD
=======
    private transient SimpleStringProperty class_name;

>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Class<? extends sport_fac> object) {
        this.object = object;
    }


    public Obj(String name, String nameToDisplay, Object obj, Class objClass) {
<<<<<<< HEAD
        this.name = name;
        class_name = nameToDisplay;
        this.object = obj;

        this.cl_name = objClass;
        if (obj != null) this.getCl_name().cast(this.getObject());
=======
        this.name = new SimpleStringProperty(name);
        class_name = new SimpleStringProperty(nameToDisplay);
        this.object = obj;
        this.cl_name = objClass;
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
    }

    public void deleteObject() {
        sport_fac obj = sport_fac.class.cast(getObject());
        obj.deleteObject();
        setObject(null);
    }

<<<<<<< HEAD
    /*private void writeObject(ObjectOutputStream s) throws IOException {
=======
    private void writeObject(ObjectOutputStream s) throws IOException {
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
        s.defaultWriteObject();
        s.writeBytes(getName());
        s.writeBytes(getClass_name());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        class_name = new SimpleStringProperty(s.readLine());
<<<<<<< HEAD
    }*/
=======
        name = new SimpleStringProperty(s.readLine());
    }
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
}
