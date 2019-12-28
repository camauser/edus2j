package edus2.application.usagereporting;

import com.google.gson.Gson;
import edus2.application.version.ApplicationInfo;
import edus2.domain.EDUS2Configuration;
import edus2.domain.SystemIdentifier;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsageReportingService {
    // TODO: Set to valid URL - maybe in config/properties file?
    private static final String USAGE_REPORTING_URL = "http://raspberrypi/statstracker.php";
    private EDUS2Configuration configuration;
    private CloseableHttpClient client;
    private Gson gson;
    private ExecutorService threadPool;

    @Inject
    public UsageReportingService(EDUS2Configuration configuration) {
        this.configuration = configuration;
        this.client = HttpClients.createDefault();
        threadPool = Executors.newSingleThreadExecutor();
        this.gson = new Gson();
    }

    public void reportStartup() {
        threadPool.submit(this::sendStartupRequest);
    }

    public void stop() {
        System.out.println("Received shutdown request");
        threadPool.shutdownNow();
    }

    private void sendStartupRequest() {
        if (!configuration.acceptedPhoneHomeWarning()) {
            System.err.println("Phone home warning not accepted, not reporting startup to server.");
            return;
        }

        try {
            HttpPost request = new HttpPost(USAGE_REPORTING_URL);
            String body = generateSystemInformationString();
            request.setEntity(new StringEntity(body));
            CloseableHttpResponse response = client.execute(request);
            // TODO: Remove after testing
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            System.err.println(String.format("Error reporting startup to server: %s", e));
        }
    }

    private String generateSystemInformationString() {
        String version = ApplicationInfo.getVersion();
        SystemIdentifier systemIdentifier = configuration.getSystemIdentifier();
        SystemInformationDto systemInformationDto = new SystemInformationDto(systemIdentifier, version);
        return gson.toJson(systemInformationDto);
    }

    private static class SystemInformationDto {
        String version;
        String systemIdentifier;

        SystemInformationDto(SystemIdentifier systemIdentifier, String version) {
            this.systemIdentifier = systemIdentifier.getSystemIdentifier();
            this.version = version;
        }
    }
}
