package sample.Serialization;

<<<<<<< HEAD
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.collections.MappingChange;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;
import sample.buildings.sport_fac;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinarySerializer {


    public void serialize(ObservableList<Obj> objectList) {
        try (Writer writer = new FileWriter("Output.json")) {
            Type collectionType = new TypeToken<HashMap<Object, String>>() {
            }.getType();
            Map<Object, String> mine = new HashMap<>();
            //System.out.println(new Gson().toJson(observableListUsers,collectionType));
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                    .setPrettyPrinting()
                    .create();
            List<sport_fac> myList = new ArrayList<sport_fac>();
            for (Obj o : objectList
            ) {
                mine.put(o.getObject(), o.getClass().toString());
            }
            //gson.toJson(objectList.get(0).getObject(), Object.class, writer);
            gson.toJson(objectList, writer);
        } catch (IOException e) {
            System.out.println("Can't create json");
        }
        /*try {
=======
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BinarySerializer {

    public  void serialize(ObservableList<Obj> objectList) {
        try {
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
            // write object to file
            FileOutputStream fos = new FileOutputStream("Objectsavefile.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new ArrayList<>(objectList));
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
<<<<<<< HEAD
        }*/
    }

    public static class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                return Class.forName(json.getAsString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class ClassTypeAd implements JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            System.out.println("Deser object");
            JsonObject root = json.getAsJsonObject();
            return sport_fac.class.cast(root);

        }

=======
        }
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101
    }

    public static ObservableList<Obj> read(Path file) {
        try {
            InputStream in = Files.newInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(in);
<<<<<<< HEAD
            List<Obj> list = (List<Obj>) ois.readObject();
=======
            List<Obj> list = (List<Obj>) ois.readObject() ;
>>>>>>> 61f113a1e46c374e8d0dc31832ad3fc23fb00101

            return FXCollections.observableList(list);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FXCollections.emptyObservableList();
    }
}
