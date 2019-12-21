package edus2.domain;

import java.util.UUID;

public class SystemIdentifier {
    private UUID systemIdentifier;

    private SystemIdentifier(UUID uuid) {
        this.systemIdentifier = uuid;
    }

    public String getSystemIdentifier() {
        return systemIdentifier.toString();
    }

    public static SystemIdentifier of(String systemIdentifier) {
        return new SystemIdentifier(UUID.fromString(systemIdentifier));
    }

    public static SystemIdentifier ofRandom() {
        return new SystemIdentifier(UUID.randomUUID());
    }
}
