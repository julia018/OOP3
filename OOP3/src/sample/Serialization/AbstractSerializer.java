package sample.Serialization;

import javafx.collections.ObservableList;
import sample.Obj;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface AbstractSerializer {
    void serialize(ObservableList<Obj> objectList, OutputStream out) throws IOException;
    ObservableList<Obj> deserialize(InputStream in);
}
