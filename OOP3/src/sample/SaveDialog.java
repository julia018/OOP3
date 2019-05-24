package sample;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import sample.Plugins.IPlugin;
import sample.Serialization.AbstractSerializer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static sample.Controller.configureSerializer;
import static sample.OpenDialog.addExtFilter;

public class SaveDialog {
    private final String DIRECTORY = "D:\\initial";
    IPlugin chosenPlugin;
    FileChooser chooser;

    public SaveDialog(IPlugin plugin, List<Class<? extends AbstractSerializer>> availableSerializers, ObservableList<Obj> objectList) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        chosenPlugin = plugin;
        String pluginMarker = "n";
        if (chosenPlugin != null) pluginMarker = chosenPlugin.getMarker();
        chooser = new FileChooser();
        chooser.setInitialDirectory(new File(DIRECTORY));
        for (Class<? extends AbstractSerializer> serializerClass : availableSerializers) {
            addExtFilter(serializerClass, pluginMarker, chooser);
        }
        File file = chooser.showSaveDialog(null);
        if (file != null) {
            AbstractSerializer serializer = configureSerializer(file.getName(), availableSerializers);
            if (plugin == null) {
                OutputStream out = new FileOutputStream(file.getName());
                //serialize
                serializer.serialize(objectList, out);
                out.close();
            } else {
                //serialize to out
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                serializer.serialize(objectList, out);
                FileOutputStream fout = new FileOutputStream(file.getName());
                //archieve
                InputStream in = new ByteArrayInputStream(out.toByteArray());
                plugin.archieve(in, fout);
                out.close();
                in.close();
                fout.close();
            }
        }
    }
}
