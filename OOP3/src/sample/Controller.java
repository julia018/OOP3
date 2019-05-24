package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Plugins.IPlugin;
import sample.Plugins.PluginException;
import sample.Serialization.*;
import sample.buildings.*;

import java.io.*;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class Controller {
    private HashMap<String, String> types_map;
    private ObservableList<Obj> obj_list = FXCollections.observableArrayList();
    private List<Class<? extends AbstractSerializer>> availableSerializers = new ArrayList<>();
    private List<IPlugin> availablePlugins = new ArrayList<>();
    private HashMap<IPlugin, String> pluginMap = new HashMap<>();
    private static final String PACK = "sample.buildings.";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 700;
    private final int TOP = 10;
    private final int RIGHT = 10;
    private final int BOTTOM = 10;
    private final int LEFT = 10;
    private final int SPACING = 5;
    private final int BUTTONSPACING = 25;

    @FXML
    private ComboBox<String> cmb_class;

    @FXML
    private Button create_btn;
    @FXML
    private TableView<Obj> obj_table;

    @FXML
    private TableColumn<Obj, String> col_obj;

    @FXML
    private TableColumn<Obj, String> col_class;

    @FXML
    private TableColumn<Obj, Void> col_actions;

    @FXML
    private Button saveButton;

    @FXML
    private Button openButton;

    @FXML
    private ComboBox<String> cmbPlugins;


    @FXML
    void open(ActionEvent event) {
        obj_list.clear();
        try {
            try {
                OpenDialog open = new OpenDialog(obj_list, availablePlugins, availableSerializers);
            } catch (Exception e) {
                throw new PluginException("Ошибка открытия файла!");
            }
        } catch (Exception e) {

        }
    }


    @FXML
    void save(ActionEvent event) {
        IPlugin chosenPlugin = searchForPlugin(pluginMap, cmbPlugins.getSelectionModel().getSelectedItem());
        try {

            try {
                SaveDialog save = new SaveDialog(chosenPlugin, availableSerializers, obj_list);
            } catch (Exception e) {
                throw new PluginException("Ошибка сохранения в файл!");
            }
        } catch (Exception e) {

        }
    }

    public static AbstractSerializer configureSerializer(String chosenFileName, List<Class<? extends AbstractSerializer>> availableSerializers) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        AbstractSerializer returnSerializer = null;
        for (Class<?> currentClass : availableSerializers) {
            if (chosenFileName.endsWith(currentClass.getAnnotation(ProcessingDescription.class).ext())) {
                Constructor<?> constructor = currentClass.getConstructor();
                returnSerializer = (AbstractSerializer) constructor.newInstance();
            }
        }
        if (returnSerializer == null) {
            throw new RuntimeException("Error file extension!");
        }
        return returnSerializer;
    }

    private ArrayList<Class<? extends AbstractSerializer>> getClassesForPackage(String pkgname) {
        ArrayList<Class<? extends AbstractSerializer>> classes = new ArrayList<>();
        // Get a File object for the package
        File directory = null;
        String fullPath;
        String relPath = pkgname.replace('.', '/');
        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        try {
            directory = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
        } catch (IllegalArgumentException e) {
            directory = null;
        }
        if (directory != null && directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {
                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);
                    try {
                        try {
                            if (AbstractSerializer.class.isAssignableFrom(Class.forName(className)) && !className.equals("sample.Serialization.AbstractSerializer"))
                                classes.add((Class<? extends AbstractSerializer>) Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            throw new PluginException("Ошибка загрузки сериализаторов");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return classes;
    }

    public List<IPlugin> loadPlugins(String pluginsFolderPath) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        File pluginsFolder = new File(pluginsFolderPath);
        File[] jars = pluginsFolder.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
        List<IPlugin> plugins = new ArrayList<>();
        if (jars != null) {
            for (File file : jars) {
                Properties result = null;
                JarFile jarFile = new JarFile(file);
                IterableEnumeration<JarEntry> jarEntries = new IterableEnumeration<>(jarFile.entries());
                for (JarEntry je : jarEntries) {
                    if (je.getName().equals("provider.properties")) {
                        try (InputStream in = jarFile.getInputStream(je)) {
                            result = new Properties();
                            result.load(in);
                        }
                    }
                }
                String fn = "";
                try {
                    if (result != null) {
                        fn = result.getProperty("class");
                    } else throw new PluginException("Cant find property file for plugin");
                } catch (Exception e) {
                    System.out.println("Problems with property file");
                }
                URL jarURL = file.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
                Class pluginClass = classLoader.loadClass(fn);
                IPlugin instance = (IPlugin) pluginClass.newInstance();
                plugins.add(instance);
            }
        }
        return plugins;
    }

    //Searching in plugin hashmap
    private IPlugin searchForPlugin(HashMap<IPlugin, String> pluginMap, String description) {
        for (Map.Entry entry : pluginMap.entrySet()) {
            if (entry.getValue().equals(description)) {
                return (IPlugin) entry.getKey();
            }
        }
        return null;
    }

    private void initializePluginsCombobox(List<IPlugin> pluginList) {
        pluginMap.put(null, "no");
        for (IPlugin plugin : pluginList) {
            if (plugin.getClass().isAnnotationPresent(ProcessingDescription.class))
                pluginMap.put(plugin, plugin.getClass().getAnnotation(ProcessingDescription.class).description());
        }
        cmbPlugins.setItems(FXCollections.observableArrayList(pluginMap.values()));
        cmbPlugins.getSelectionModel().select(0);
    }

    @FXML
    void initialize() {
        availableSerializers = getClassesForPackage("sample.Serialization");
        try {
            try {
                availablePlugins = loadPlugins("plugins");
            } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new PluginException("Ошибка загрузки плагинов");
            }
        } catch (Exception e) {

        }
        initializePluginsCombobox(availablePlugins);
        obj_list.addListener((ListChangeListener<Obj>) c -> obj_table.setItems(obj_list));

        cmb_class.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (observable.getValue() == null) {
                    create_btn.setDisable(true);
                } else create_btn.setDisable(false);
            }
        });

        types_map = new HashMap<>();
        createHashMap(types_map, cmb_class);

        col_obj.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_class.setCellValueFactory(new PropertyValueFactory<>("class_name"));
        col_actions.setCellFactory(param -> new TableCell<Obj, Void>() {
            private final Button editButton = new Button("edit");
            private final Button deleteButton = new Button("delete");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                pane.setSpacing(BUTTONSPACING);

                deleteButton.setOnAction(event -> {
                    Obj deletedObj = getTableView().getItems().get(getIndex());
                    deletedObj.deleteObject();
                    obj_list.remove(deletedObj);
                });

                editButton.setOnAction(event -> {
                    try {

                        String title = getTableView().getItems().get(getIndex()).getName();
                        Class clazz = getTableView().getItems().get(getIndex()).getCl_name();
                        Object my_obj = getTableView().getItems().get(getIndex()).getObject();
                        ArrayList<Field> fieldList = new ArrayList<>();
                        Class i = clazz;
                        while (i != null && i != Object.class) {
                            Collections.addAll(fieldList, i.getDeclaredFields());
                            i = i.getSuperclass();
                        }
                        Collections.reverse(fieldList);

                        VBox vb = new VBox();
                        vb.setSpacing(SPACING);
                        vb.setPadding(new Insets(TOP, RIGHT, BOTTOM, LEFT));

                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setTitle(title);
                        stage.sizeToScene();

                        List<Control> controlList = new ArrayList<>();
                        generateControls(vb, controlList, fieldList, my_obj, clazz);

                        Button btnDone = new Button("DONE");
                        btnDone.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                String obj_name = "";
                                try {
                                    ArrayList<Method> mainMethodList = new ArrayList<>();
                                    try {
                                        clazz.cast(my_obj);
                                        Class p = clazz;
                                        while (p != null && p != Object.class) {
                                            Collections.addAll(mainMethodList, p.getDeclaredMethods());
                                            p = p.getSuperclass();
                                        }
                                        for (Control r : controlList) {
                                            if (r.getType().equals("name")) obj_name = (String) r.getVal();
                                            if (r.getClass() == LabelNew.class) { // composition
                                                Class clazzz = r.getCl();
                                                Object comp_obj = r.getObj();
                                                clazzz.cast(comp_obj);
                                                ArrayList<Method> methodList = new ArrayList<>();
                                                Collections.addAll(methodList, clazzz.getDeclaredMethods());
                                                for (Method t : methodList) {
                                                    for (Control s : controlList) {
                                                        if (t.getName().equals("set" + s.getType())) {
                                                            t.invoke(comp_obj, s.getVal());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        for (Method m : mainMethodList) {
                                            for (Control r : controlList) {
                                                if (m.getName().equals("set" + r.getType())) {
                                                    m.invoke(my_obj, r.getVal());
                                                }
                                            }
                                        }
                                        getTableView().getItems().get(getIndex()).setObject(null);
                                        obj_list.remove(getTableView().getItems().get(getIndex()));
                                        obj_list.add(new Obj(obj_name, my_obj.getClass().getAnnotation(RusName.class).r_name(), my_obj, my_obj.getClass()));
                                        stage.close();

                                    } catch (ClassCastException el) {
                                        System.out.println("Can't cast!");
                                    }
                                } catch (IllegalAccessException | InvocationTargetException l) {
                                    System.out.println("Reflection exception!");
                                }
                            }
                        });

                        HBox hb = new HBox();
                        hb.setAlignment(Pos.CENTER);
                        hb.getChildren().add(btnDone);
                        vb.getChildren().add(hb);
                        stage.setTitle(title);
                        stage.setScene(new Scene(vb, WIDTH, HEIGHT));
                        stage.show();
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        System.out.println("Reflection exception!");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        addObjects();
    }


    @FXML
    void create(ActionEvent event) {
        String selected_class = Func(types_map, cmb_class.getValue());
        String title = cmb_class.getValue();
        cmb_class.setValue(null);
        try {
            Class clazz = Class.forName(PACK + selected_class);
            ArrayList<Field> fieldList = new ArrayList<Field>();
            Class i = clazz;
            while (i != null && i != Object.class) {
                Collections.addAll(fieldList, i.getDeclaredFields());
                i = i.getSuperclass();
            }
            Collections.reverse(fieldList);

            VBox vb = new VBox();
            vb.setSpacing(SPACING);
            vb.setPadding(new Insets(TOP, RIGHT, BOTTOM, LEFT));

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.sizeToScene();

            List<Control> controlsList = new ArrayList<>();
            try {
                generateControls(vb, controlsList, fieldList, null, null);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                System.out.println("Reflection exception!");
            }

            Button btnCreate = new Button("CREATE");
            btnCreate.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    String obj_name = "";
                    Constructor<?> constructor;
                    try {
                        ArrayList<Method> methodList = new ArrayList<>();
                        constructor = clazz.getConstructor();
                        Object instance = constructor.newInstance();
                        clazz.cast(instance);
                        Class p = clazz;
                        while (p != null && p != Object.class) {
                            Collections.addAll(methodList, p.getDeclaredMethods());
                            p = p.getSuperclass();
                        }
                        for (Control r : controlsList) {
                            if (r.getType().equals("name")) {
                                obj_name = (String) r.getVal();
                            }
                            if (r.getClass() == LabelNew.class) { // composition
                                Class clazzz = r.getCl();
                                constructor = clazzz.getConstructor();
                                Object inst = constructor.newInstance();
                                clazzz.cast(inst);
                                ArrayList<Method> compMethodsList = new ArrayList<>();
                                Collections.addAll(compMethodsList, clazzz.getDeclaredMethods());
                                for (Method t : compMethodsList) {
                                    for (Control s : controlsList) {
                                        if (t.getName().equals("set" + s.getType())) {
                                            t.invoke(inst, s.getVal());
                                        }
                                    }
                                }
                                r.setObj(inst);
                            }
                        }
                        for (Method m : methodList) {
                            for (Control r : controlsList) {
                                if (m.getName().equals("set" + r.getType())) {
                                    m.invoke(instance, r.getVal());
                                }
                            }
                        }

                        obj_list.add(new Obj(obj_name, instance.getClass().getAnnotation(RusName.class).r_name(), instance, instance.getClass()));
                        stage.close();
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException l) {
                        System.out.println("Reflection exception!");
                    }
                }
            });

            HBox hb = new HBox();
            hb.setAlignment(Pos.CENTER);
            hb.getChildren().add(btnCreate);
            vb.getChildren().add(hb);
            stage.setScene(new Scene(vb, WIDTH, HEIGHT));
            stage.show();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found!");
        }
    }

    void generateControls(VBox vbox, List<Control> controlList, ArrayList<Field> fieldList, Object object, Class objClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Field field : fieldList) {
            String controlName, controlType;
            //Текстовое поле
            if (field.getType() == String.class) {
                controlType = field.getName();
                controlName = field.getAnnotation(RusName.class).r_name();
                String prompt = "";
                if (object != null) {
                    Method get_f = objClass.getMethod("get" + controlType);
                    prompt = (String) get_f.invoke(object);
                }
                generateTxtField(vbox, controlList, controlName, controlType, prompt);
            }
            //Целочисленное поле
            if (field.getType() == int.class) {
                controlType = field.getName();
                controlName = field.getAnnotation(RusName.class).r_name();
                Integer prompt = 0;
                if (object != null) {
                    Method get_f = objClass.getMethod("get" + controlType);
                    prompt = (Integer) get_f.invoke(object);
                }
                generateTxtField(vbox, controlList, controlName, controlType, prompt);
            }
            //Вещественное поле
            if (field.getType() == float.class) {
                controlType = field.getName();
                controlName = field.getAnnotation(RusName.class).r_name();
                Float prompt = (float) 0;
                if (object != null) {
                    Method getterMethod = objClass.getMethod("get" + controlType);
                    prompt = (Float) getterMethod.invoke(object);
                }
                generateTxtField(vbox, controlList, controlName, controlType, prompt);
            }
            //Перечисление(enum)
            if (field.getType().isEnum()) {
                controlType = field.getName();
                controlName = field.getAnnotation(RusName.class).r_name();
                Label lbl_num = new Label(controlName);
                vbox.getChildren().add(lbl_num);
                ArrayList<Field> en_flds = new ArrayList<>();
                Collections.addAll(en_flds, field.getType().getFields());
                String resultl = "";
                if (object != null) {
                    Method get_f = objClass.getMethod("get" + field.getName());
                    resultl = (String) get_f.invoke(object);
                }
                // list of fields-constants declared annotations
                ArrayList<String> arr = new ArrayList<>();
                Object ill = null;
                for (Field fld : en_flds) {
                    arr.add(fld.getAnnotation(RusName.class).r_name());
                    if (fld.getName().equals(resultl) && object != null)
                        ill = fld.getAnnotation(RusName.class).r_name();
                }
                ChoiceBoxNew chBox = new ChoiceBoxNew(FXCollections.observableArrayList(arr), controlType, ill, en_flds);
                controlList.add(chBox);
                vbox.getChildren().add(chBox);
            }
            //Объект(композиция)
            if (Composition.class.isAssignableFrom(field.getType())) {
                Separator sp1 = new Separator(Orientation.HORIZONTAL);
                vbox.getChildren().add(sp1);
                ArrayList<Field> comp_fields = new ArrayList<>();
                Collections.addAll(comp_fields, field.getType().getDeclaredFields());
                controlName = field.getAnnotation(RusName.class).r_name();
                controlType = field.getName();
                LabelNew lbl_num = new LabelNew(controlName, field.getType(), controlType);
                controlList.add(lbl_num);
                HBox hb = new HBox();
                hb.setAlignment(Pos.CENTER);
                hb.getChildren().add(lbl_num);
                vbox.getChildren().add(hb);
                Object compObject = null;
                Class compClass = null;

                if (object != null) {
                    compClass = field.getType();
                    Method get_comp = object.getClass().getMethod("get" + field.getName());
                    compObject = get_comp.invoke(object);//get comp obj-> res
                    compClass.cast(compObject);
                    lbl_num.setObj(compObject);
                }

                generateControls(vbox, controlList, comp_fields, compObject, compClass);
                Separator sp2 = new Separator(Orientation.HORIZONTAL);
                vbox.getChildren().add(sp2);
            }
            //Выбор(логическое поле)
            if ((field.getType() == Boolean.class)) {
                controlName = field.getAnnotation(RusName.class).r_name();
                controlType = field.getName();
                Boolean res = false;
                if (object != null) {
                    Method get_f = objClass.getMethod("get" + field.getName());
                    res = (Boolean) get_f.invoke(object);
                }
                CheckBoxNew ch_b = new CheckBoxNew(controlName, controlType, Boolean.valueOf(res));
                vbox.getChildren().add(ch_b);
                controlList.add(ch_b);
            }
        }
    }

    private void generateTxtField(VBox vb, List<Control> controlList, String title, String type, Object prompt) {
        TextFieldNew textField = null;
        Label label = new Label(title);
        vb.getChildren().add(label);
        if (prompt.getClass() == String.class) {
            textField = new TextFieldNew((String) prompt, type);
        } else {
            if (prompt.getClass() == Integer.class) {
                textField = new TextFieldNew((int) prompt, type);
            } else {
                if (prompt.getClass() == Float.class) {
                    textField = new TextFieldNew((float) prompt, type);
                }
            }
        }
        controlList.add(textField);
        vb.getChildren().add(textField);
    }

    //Searching in HM
    private String Func(HashMap<String, String> HM, String K) {
        for (Map.Entry entry : HM.entrySet()) {
            if (entry.getKey().equals(K)) {
                return entry.getValue().toString();
            }
        }
        return "";
    }

    //Initializing HashMap and filling ComboBox
    private void createHashMap(HashMap<String, String> typesMap, ComboBox<String> comboBox) {
        typesMap.put("Лазерный тир", "laser_shoot");
        typesMap.put("Пневманический тир", "pnum_shoot");
        typesMap.put("Теннисный корт", "tennis_cort");
        typesMap.put("Бассейн", "pool");
        typesMap.put("Легкоатлетический стадион", "athlete_stadium");
        typesMap.put("Футбольный стадион", "football_stadium");
        comboBox.setItems(FXCollections.observableArrayList(types_map.keySet()));
        comboBox.getSelectionModel().select(0);
    }

    private void addObjects() {
        football_stadium FS = createStadium();

        //элемент таблицы
        Obj itemFootStadium = new Obj(FS.getname(), "Футбольный стадион", FS, FS.getClass());
        obj_list.add(itemFootStadium);

        //Сектор трека
        track Track = new track();
        Track.setamount("greater_five");
        Track.settr_length("100");

        //Атлетический стадион
        athlete_stadium newAthleteSt = new athlete_stadium();
        newAthleteSt.settrack_sector(Track);
        newAthleteSt.setpit(true);
        newAthleteSt.setcapacity("200000");
        newAthleteSt.setfl_amount("5");
        newAthleteSt.setlocation("Сен-Дени");
        newAthleteSt.setlight_type("natural");
        newAthleteSt.setname("Стад де Франс");

        Obj itemAthlStadium = new Obj(newAthleteSt.getname(), "Легкоатлетический стадион", newAthleteSt, newAthleteSt.getClass());
        obj_list.add(itemAthlStadium);


        //Бассейн
        pool swPool = new pool();
        swPool.setdes("natrium");
        swPool.setdividers(false);
        swPool.setcapacity("20000");
        swPool.setfl_amount("8");
        swPool.setlocation("Кипр");
        swPool.setname("Four Seasons");
        swPool.setlight_type("synthetic");

        Obj itemPool = new Obj(swPool.getname(), "Бассейн", swPool, swPool.getClass());
        obj_list.add(itemPool);
        obj_table.setItems(obj_list);
    }

    public static football_stadium createStadium() {
        //Поле
        field myField = new field();
        myField.setgr_type("natural");
        myField.setfence(true);

        //Ворота
        gate Gates = new gate();
        Gates.setheight("50");
        Gates.setwidth("100");
        Gates.setmobility(true);

        //Стадион
        football_stadium FS = new football_stadium();
        FS.setpl_field(myField);
        FS.setpl_gate(Gates);
        FS.setname("Лужники");
        FS.setteam("Спартак");
        FS.setcapacity("200000");
        FS.setfl_amount("10");
        FS.setvip(false);
        FS.setlight_type("synthetic");
        FS.setlocation("Россия, Москва");
        FS.setparking("100000");
        return FS;
    }

}
