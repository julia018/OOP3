package sample.Serialization;

import javafx.collections.ObservableList;
import sample.Obj;

import java.io.File;

public interface SerializerFactory {
    void serialize(ObservableList<Obj> objectList, File chosenFileForSave);
    ObservableList<Obj> deserialize(File chosenFileForOpen);
}
