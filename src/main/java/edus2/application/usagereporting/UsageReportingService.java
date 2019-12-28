package edus2.application.usagereporting;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
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
import java.util.Optional;

public class UsageReportingService {
    private EDUS2Configuration configuration;
    private CloseableHttpClient client;
    private Gson gson;

    @Inject
    public UsageReportingService(EDUS2Configuration configuration) {
        this.configuration = configuration;
        this.client = HttpClients.createDefault();
        this.gson = new Gson();
    }

    public Optional<String> reportStartup() {
        if (!configuration.acceptedPhoneHomeWarning()) {
            System.err.println("Phone home warning not accepted, not reporting startup to server.");
            return Optional.empty();
        }

        try {
            HttpPost request = new HttpPost(ApplicationInfo.getStatisticsReportingUrl());
            String body = generateSystemInformationString();
            request.setEntity(new StringEntity(body));
            CloseableHttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            ResponseDto serverResponse = gson.fromJson(responseBody, ResponseDto.class);
            return Optional.ofNullable(serverResponse.message);
        } catch (JsonParseException e) {
            System.err.println(String.format("Error parsing server response: %s", e));
        } catch (Exception e) {
            System.err.println(String.format("Error reporting startup to server: %s", e));
        }
        return Optional.empty();
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

    private static class ResponseDto {
        String message;
    }
}
