package sample.Serialization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinarySerializer implements SerializerFactory{

    @Override
    public void serialize(ObservableList<Obj> objectList, File fileForSave) {
        try {
            // write object to file
            FileOutputStream fos = new FileOutputStream(fileForSave);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(objectList));
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Obj> deserialize(File fileForOpen) {

        List<Obj> loadedEdges = new ArrayList<>();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileForOpen))) {
                loadedEdges = (List<Obj>) in.readObject() ;
                //obj_list.setAll(loadedEdges);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

        return FXCollections.observableList(loadedEdges);
    }
}
