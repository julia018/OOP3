package sample.Serialization;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;
import sample.RusName;
import sample.buildings.sport_fac;

import java.io.*;
import java.lang.reflect.Type;

public class JSONSerializer implements SerializerFactory{
    public void serialize(ObservableList<Obj> objectList, File fileForSave) {
        try (Writer writer = new FileWriter(fileForSave)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                    .setPrettyPrinting()
                    .create();

            gson.toJson(objectList, writer);
        } catch (IOException e) {
            System.out.println("Can't create json");
        }
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

    public ObservableList<Obj> deserialize(File fileForOpen) {
        ObservableList<Obj> obj_list = FXCollections.observableArrayList();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileForOpen));
        } catch (FileNotFoundException e) {
            System.out.println("Not file here!");
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                .setPrettyPrinting()
                .create();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(bufferedReader);
        JsonArray arr = jsonElement.getAsJsonArray();
        arr.forEach(item -> {
            JsonObject obj = (JsonObject) item;
            JsonObject quoteid = obj.get("object").getAsJsonObject();

            String dateEntered = obj.get("cl_name").getAsString();
            try {
                Class<?> cl = Class.forName(dateEntered);
                Object message = gson.fromJson(quoteid, cl);
                System.out.println(message.getClass());
                Obj o = new Obj(((sport_fac) message).getname(), message.getClass().getAnnotation(RusName.class).r_name(), message, message.getClass());
                obj_list.add(o);
            } catch (ClassNotFoundException e) {
                System.out.println("Cant found classss");
            }

        });
        return obj_list;
    }
}
