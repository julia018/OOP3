package sample.Plugins;

import javafx.scene.control.Alert;

public class PluginException extends Exception{
    private final String ERROR = "Ошибка";
    public PluginException(String message) {
        infoBox(message, ERROR, null);
        return;
    }

    public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
