package sample.Serialization;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;
import sample.ProcessingDescription;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ProcessingDescription(description = "bin", ext = "bin")
public class BinarySerializer implements AbstractSerializer {

    public void serialize(ObservableList<Obj> objectList, OutputStream out) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(new ArrayList<>(objectList));
            oos.close();
            out.close();
        } catch (Exception ex) {
            throw  new RuntimeException("Can't serialize");
        }

    }

    public ObservableList<Obj> deserialize(InputStream in) {
        List<Obj> loadedObjects = new ArrayList<>();
        try {
            ObjectInputStream ino = new ObjectInputStream(in);
            loadedObjects = (List<Obj>) ino.readObject();
            in.close();
            ino.close();
        } catch (Exception ex) {
            throw new RuntimeException("Can't deserialize");
        }
        return FXCollections.observableList(loadedObjects);
    }
}
