package sample.Plugins;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IPlugin {
    void archieve(InputStream in, OutputStream out) throws IOException;
    void dearchieve(InputStream in, OutputStream out) throws IOException;
    String getMarker();
}
