package edus2.application.version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationInfo {

    private static final String VERSION_FILE = "version.txt";
    private static final String VERSION_KEY = "version";

    public static String getVersion() {
        try {
            InputStream versionStream = ApplicationInfo.class.getClassLoader().getResourceAsStream(VERSION_FILE);
            Properties properties = new Properties();
            properties.load(versionStream);
            return properties.getProperty(VERSION_KEY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
