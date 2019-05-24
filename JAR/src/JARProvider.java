import sample.Plugins.IPlugin;
import sample.ProcessingDescription;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

@ProcessingDescription(description = "jar", ext = "jar")
public class JARProvider implements IPlugin {
    private final String ENTRY_NAME = "list";

    @Override
    public void archieve(InputStream in, OutputStream out) throws IOException {
        final int BUFFER = 2048;
        byte buffer[] = new byte[BUFFER];
        JarOutputStream zos = new JarOutputStream(out);
        zos.putNextEntry(new JarEntry(ENTRY_NAME));
        int length;
        while ((length = in.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }
        zos.closeEntry();
        zos.close();
    }

    @Override
    public void dearchieve(InputStream in, OutputStream out) throws IOException {
        JarInputStream zin = new JarInputStream(in);
        ZipEntry entry;

        while ((entry = zin.getNextEntry()) != null) {
            if (entry.getName().equals(ENTRY_NAME)) {
                final int BUFFER = 2048;
                byte buffer[] = new byte[BUFFER];

                int size;
                while ((size = zin.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, size);
                }

                break;
            }
        }
    }

    @Override
    public String getMarker() {
        return "j";
    }
}
