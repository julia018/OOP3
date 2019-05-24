package sample;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import sample.Plugins.IPlugin;
import sample.Serialization.AbstractSerializer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static sample.Controller.configureSerializer;

public class OpenDialog {
    private final String DIRECTORY = "D:\\initial";
    FileChooser chooser;
    public OpenDialog(ObservableList<Obj> objectList, List<IPlugin> availablePlugins, List<Class<? extends AbstractSerializer>> availableSerializers) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        chooser = new FileChooser();
        chooser.setInitialDirectory(new File(DIRECTORY));
        for (Class<? extends AbstractSerializer> serializerClass : availableSerializers) {
            addExtFilter(serializerClass, "n", chooser); // add no plugin
            for (IPlugin plugin : availablePlugins) {
                addExtFilter(serializerClass, plugin.getMarker(), chooser);
            }
        }
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            String name = file.getName();
            String extension = name.substring(name.lastIndexOf("."));
            AbstractSerializer serializer = configureSerializer(extension, availableSerializers);
            String pluginMarker = extension.substring(1, 2);
            IPlugin currentPlugin = searchForPlugin(availablePlugins, pluginMarker);
            //without plugin
            if (currentPlugin == null) {
                ObservableList<Obj> res= serializer.deserialize(new FileInputStream(file.getName()));
                objectList.addAll(res);
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                currentPlugin.dearchieve(new FileInputStream(file.getName()), out);
                ObservableList<Obj> res = serializer.deserialize(new ByteArrayInputStream(out.toByteArray()));
                objectList.addAll(res);
           }
        }
    }

    private IPlugin searchForPlugin(List<IPlugin> pluginList, String pluginMarker) {
        for (IPlugin plugin : pluginList) {
            if (plugin.getMarker().equals(pluginMarker)) {
                return plugin;
            }
        }
        return null;
    }

    static void addExtFilter(Class<? extends AbstractSerializer> serializerClass, String pluginMarker, FileChooser currChooser) {
        String serializerDescription = String.format("%s%s", pluginMarker, serializerClass.getAnnotation(ProcessingDescription.class).description());
        String serializerExt = String.format("*.%s%s", pluginMarker, serializerClass.getAnnotation(ProcessingDescription.class).ext());
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(serializerDescription, serializerExt);
        currChooser.getExtensionFilters().add(extensionFilter);
    }


}
