package edus2.application.version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationInfo {

    private static final String PROPERTIES_FILE = "edus2j.properties";
    private static final String VERSION_KEY = "version";
    private static final String STATISTICS_URL_KEY = "stats.url";

    public static String getVersion() {
        return readProperty(VERSION_KEY);
    }

    public static String getStatisticsReportingUrl() {
        return readProperty(STATISTICS_URL_KEY);
    }

    private static String readProperty(String key) {
        try {
            InputStream propertyStream = ApplicationInfo.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            Properties properties = new Properties();
            properties.load(propertyStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
