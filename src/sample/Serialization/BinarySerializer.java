package sample.Serialization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinarySerializer implements AbstractSerializer {

    public void serialize(ObservableList<Obj> objectList, File fileForSave) {
        try {
            FileOutputStream fos = new FileOutputStream(fileForSave);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(objectList));
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось найти файл для записи!");
        } catch (IOException e) {
            System.out.println("Не удалось сериализовать данные!");
        }
    }

    public ObservableList<Obj> deserialize(File fileForOpen) {
        List<Obj> loadedObjects = new ArrayList<>();
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileForOpen))) {
                loadedObjects = (List<Obj>) in.readObject() ;
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        return FXCollections.observableList(loadedObjects);
    }
}
