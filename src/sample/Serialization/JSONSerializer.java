package sample.Serialization;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Obj;
import sample.RusName;
import sample.buildings.sport_fac;

import java.io.*;
import java.lang.reflect.Type;

public class JSONSerializer implements AbstractSerializer {

    public void serialize(ObservableList<Obj> objectList, File fileForSave) {
        try (Writer writer = new FileWriter(fileForSave)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                    .setPrettyPrinting()
                    .create();

            gson.toJson(objectList, writer);
        } catch (IOException e) {
            System.out.println("Не удалось сериализовать объекты!");
        }
    }

    private static class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

        @Override
        public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getName());
        }

        @Override
        public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Class.forName(json.getAsString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Не удалось обнаружить класс десериализуемого объекта!");
            }
        }
    }

    public ObservableList<Obj> deserialize(File fileForOpen) {
        ObservableList<Obj> objList = FXCollections.observableArrayList();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(fileForOpen));
        } catch (FileNotFoundException e) {
            System.out.println("Не удалось найти файд!");
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                .setPrettyPrinting()
                .create();

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(bufferedReader);
        JsonArray arr = jsonElement.getAsJsonArray();
        parseJSONArray(objList, arr, gson);
        return objList;
    }

    private ObservableList<Obj> parseJSONArray(ObservableList<Obj> objectList, JsonArray arrayForParse, Gson gson) {
        arrayForParse.forEach(item -> {
            JsonObject obj = (JsonObject) item;
            JsonObject jsonObject = obj.get("object").getAsJsonObject();
            String className = obj.get("cl_name").getAsString();
            try {
                Class<?> cl = Class.forName(className);
                Object newObject = gson.fromJson(jsonObject, cl);
                Obj o = new Obj(((sport_fac) newObject).getname(), newObject.getClass().getAnnotation(RusName.class).r_name(), newObject, newObject.getClass());
                objectList.add(o);
            } catch (ClassNotFoundException e) {
                System.out.println("Не удалось обнаружить класс при десериализации!");
            }
        });
        return objectList;
    }
}
