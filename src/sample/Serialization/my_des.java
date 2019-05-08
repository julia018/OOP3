package sample.Serialization;

import com.google.gson.*;
import sample.buildings.sport_fac;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class my_des implements JsonDeserializer<sport_fac> {
    private String animalTypeElementName;
    private Gson gson;
    private Map<String, Class<? extends sport_fac>> animalTypeRegistry;

    public my_des(String animalTypeElementName) {
        this.animalTypeElementName = animalTypeElementName;
        this.gson = new Gson();
        this.animalTypeRegistry = new HashMap<>();
    }

    public void registerBarnType(String animalTypeName, Class<? extends sport_fac> animalType) {
        animalTypeRegistry.put(animalTypeName, animalType);
    }

    public sport_fac deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject animalObject = json.getAsJsonObject();
        JsonElement animalTypeElement = animalObject.get(animalTypeElementName);

        Class<? extends sport_fac> animalType = animalTypeRegistry.get(animalTypeElement.getAsString());
        return gson.fromJson(animalObject, animalType);
    }
}
