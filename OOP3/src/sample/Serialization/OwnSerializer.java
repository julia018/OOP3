package sample.Serialization;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Composition;
import sample.Obj;
import sample.ProcessingDescription;
import sample.RusName;
import sample.buildings.sport_fac;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@ProcessingDescription(description = "own", ext = "txt")
public class OwnSerializer implements AbstractSerializer {

    String serializeObject(Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder resultString = new StringBuilder("{");
        if (o == null) {
            resultString.append("null};");
            return resultString.toString();
        }
        resultString.append(o.getClass().getName()).append(";");
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
            resultString.append(objectField.getName()).append(":");
            if (Composition.class.isAssignableFrom(objectField.getType())) {
                resultString.append(serializeInnerObject(objectField, o));
            } else {
                for (Method objectMethod : mainMethodList) {
                    if (objectMethod.getName().equals("get" + objectField.getName())) {
                        String value = objectMethod.invoke(o).toString();
                        resultString.append(value).append(";");
                    }
                }
            }
        }
        resultString.append("}");
        return resultString.toString();
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
        StringBuilder mainClassName = new StringBuilder();
        ArrayList<Method> methodList = new ArrayList<>();
        HashMap<String, String> mainObjFields = new HashMap<>();
        while (!(objectInString.charAt(i) == ';')) {
            mainClassName.append(objectInString.charAt(i));
            i++;
        }
        i++;
        resultObject = createObject(mainClassName.toString());
        Class mainClass = Class.forName(mainClassName.toString());
        mainClass.cast(resultObject);
        Class a = mainClass;
        while (a != null && a != Object.class) {
            Collections.addAll(methodList, a.getDeclaredMethods());
            a = a.getSuperclass();
        }
        while (i != (objectInString.length() - 1)) {
            StringBuilder key = new StringBuilder();
            StringBuilder value = new StringBuilder();
            while (!(objectInString.charAt(i) == ':')) {
                key.append(objectInString.charAt(i));
                i++;
            }
            i++;
            //object in composition
            if (objectInString.charAt(i) == '{') {
                i++;
                StringBuilder className = new StringBuilder();
                while (!(objectInString.charAt(i) == ';')) {
                    className.append(objectInString.charAt(i));
                    i++;
                }
                i++;
                Object compObject = createObject(className.toString());
                Class<?> compClass = Class.forName(className.toString());
                compClass.cast(compObject);
                ArrayList<Method> compMethodsList = new ArrayList<>();
                Collections.addAll(compMethodsList, compClass.getDeclaredMethods());
                HashMap<String, String> objFields = new HashMap<>();
                while (!(objectInString.charAt(i) == '}')) {
                    StringBuilder objectFieldDescription = new StringBuilder();
                    while (!(objectInString.charAt(i) == ';')) {
                        objectFieldDescription.append(objectInString.charAt(i));
                        i++;
                    }
                    String[] out = objectFieldDescription.toString().split(":");
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
                while (!(objectInString.charAt(i) == ';')) {
                    value.append(objectInString.charAt(i));
                    i++;
                }
                i++;
                mainObjFields.put(key.toString(), value.toString());
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
        Class clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor();
        Object newObject = constructor.newInstance();
        clazz.cast(newObject);
        return newObject;
    }

    public void serialize(ObservableList<Obj> objectList, OutputStream out) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            for (Obj o : objectList) {
                try {
                    writeObject(o, writer);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            out.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeObject(Obj o, OutputStreamWriter writer) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        String serObj = serializeObject(o.getObject());
        writer.write(serObj);
        writer.append("\r\n");
        writer.flush();
    }

    public ObservableList<Obj> deserialize(InputStream in) {
        ObservableList<Obj> obj_list = FXCollections.observableArrayList();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                try {
                    sport_fac obj = (sport_fac) deserializeObject(strLine);
                    obj_list.add(new Obj(obj.getname(), obj.getClass().getAnnotation(RusName.class).r_name(), obj, obj.getClass()));
                    in.close();
                    br.close();
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
        } catch (IOException e) {
            System.out.println("Ошибка открытия файла!");
        }
        return obj_list;
    }
}
