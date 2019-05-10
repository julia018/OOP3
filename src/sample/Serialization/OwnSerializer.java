package sample.Serialization;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Composition;
import sample.Obj;
import sample.RusName;
import sample.buildings.sport_fac;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class OwnSerializer implements AbstractSerializer {

    String serializeObject(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String resultString = "{";
        if (o == null) {
            resultString += "null};";
            return resultString;
        }
        resultString += o.getClass().getName() + ";";
        ArrayList<Field> fieldList = new ArrayList<>();
        ArrayList<Method> mainMethodList = new ArrayList<>();
        Class i = o.getClass();
        while (i != null && i != Object.class) {
            Collections.addAll(fieldList, i.getDeclaredFields());
            Collections.addAll(mainMethodList, i.getDeclaredMethods());
            i = i.getSuperclass();
        }
        Collections.reverse(fieldList);
        for (Field objectField : fieldList) {
            resultString += objectField.getName() + ":";
            if(Composition.class.isAssignableFrom(objectField.getType())){
                resultString += serializeInnerObject(objectField, o);
            } else {
                for (Method objectMethod : mainMethodList) {
                    if (objectMethod.getName().equals("get" + objectField.getName())) {
                        String value = objectMethod.invoke(o).toString();
                        resultString += value + ";";
                    }
                }
            }
        }
            resultString += "}";
            return resultString;
        }

        private String serializeInnerObject(Field compObjectField, Object mainObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Class<?> compClass = compObjectField.getType();
            Method getCompObject = mainObject.getClass().getMethod("get" + compObjectField.getName());
            Object compObject = getCompObject.invoke(mainObject);
            compClass.cast(compObject);
            return serializeObject(compObject);
        }

        Object deserializeObject(String objectInString) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            int i = 1;
            Object resultObject;
            String mainClassName = "";
            ArrayList<Method> methodList = new ArrayList<>();
            HashMap<String, String> mainObjFields = new HashMap<>();
            while(!(objectInString.charAt(i) == ';')) {
                mainClassName += objectInString.charAt(i);
                i++;
            }
            i++;
            resultObject = createObject(mainClassName);
            Class mainClass = Class.forName(mainClassName);
            mainClass.cast(resultObject);
            Class a = mainClass;
            while (a != null && a != Object.class) {
                Collections.addAll(methodList, a.getDeclaredMethods());
                a = a.getSuperclass();
            }
            while (i != (objectInString.length()-1)) {
                String key = "";
                String value = "";
                while(!(objectInString.charAt(i) == ':')) {
                    key += objectInString.charAt(i);
                    i++;
                }
                i++;
                //object in composition
                if(objectInString.charAt(i) == '{') {
                    i++;
                    String className = "";
                    while(!(objectInString.charAt(i) == ';')) {
                       className += objectInString.charAt(i);
                       i++;
                    }
                    i++;
                    Object compObject = createObject(className);
                    Class<?> compClass = Class.forName(className);
                    compClass.cast(compObject);
                    ArrayList<Method> compMethodsList = new ArrayList<>();
                    Collections.addAll(compMethodsList, compClass.getDeclaredMethods());
                    HashMap<String, String> objFields = new HashMap<>();
                    while(!(objectInString.charAt(i) == '}')){
                        String objectFieldDescription = "";
                        while(!(objectInString.charAt(i) == ';')){
                            objectFieldDescription += objectInString.charAt(i);
                            i++;
                        }
                        String[] out = objectFieldDescription.split(":");
                        String fieldName = out[0];
                        String fieldValue = out[1];
                        objFields.put(fieldName, fieldValue);
                        i++;
                    }
                    i++;
                    setObjectFields(compMethodsList, objFields.entrySet(), compObject);
                    //set inner object to main
                    for (Method mainObjMethod : methodList) {
                            if (mainObjMethod.getName().equals("set" + key)) {
                                mainObjMethod.invoke(resultObject, compObject);
                            }
                    }
                } else {
                    while(!(objectInString.charAt(i) == ';')) {
                        value += objectInString.charAt(i);
                        i++;
                    }
                    i++;
                    mainObjFields.put(key,value);
                }
            }
            setObjectFields(methodList, mainObjFields.entrySet(), resultObject);
            return resultObject;
        }

    private void setObjectFields(ArrayList<Method> methodList, Set<Map.Entry<String, String>> entrySet, Object resultObject) throws InvocationTargetException, IllegalAccessException {
        for (Method mainObjectMethod : methodList) {
            for (Map.Entry entry : entrySet) {
                if (("set" + entry.getKey()).equals(mainObjectMethod.getName())) {
                    if (mainObjectMethod.getParameterTypes()[0].equals(Boolean.class)) {
                        mainObjectMethod.invoke(resultObject, Boolean.valueOf((String) entry.getValue()));
                    } else
                        mainObjectMethod.invoke(resultObject, (String) entry.getValue());
                }
            }
        }
    }

    Object createObject(String className) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException {
        if(className == "null") {
            return null;
        }
        Class clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor();
        Object newObject = constructor.newInstance();
        clazz.cast(newObject);
        return newObject;
    }

    public void serialize(ObservableList<Obj> objectList, File fileForSave) {
        try {
            FileWriter writer = new FileWriter(fileForSave, false);
            OwnSerializer own = new OwnSerializer();
            for(Obj o: objectList) {
                try {
                    writeObject(own, o, writer);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeObject(OwnSerializer own, Obj o, FileWriter writer) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        String serObj = own.serializeObject(o.getObject());
        writer.write(serObj);
        writer.append("\r\n");
        writer.flush();
    }

    public ObservableList<Obj> deserialize(File fileForOpen) {
        ObservableList<Obj> obj_list = FXCollections.observableArrayList();
        try{
            FileInputStream fstream = new FileInputStream(fileForOpen);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null){
                try {
                    sport_fac obj = (sport_fac) deserializeObject(strLine);
                    obj_list.add(new Obj(obj.getname(), obj.getClass().getAnnotation(RusName.class).r_name(), obj, obj.getClass()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            System.out.println("Ошибка открытия файла!");
        }
        return obj_list;
    }
}
