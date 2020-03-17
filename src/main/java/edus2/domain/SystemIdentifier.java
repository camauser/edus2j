package edus2.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SystemIdentifier that = (SystemIdentifier) o;

        return new EqualsBuilder()
                .append(systemIdentifier, that.systemIdentifier)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(systemIdentifier)
                .toHashCode();
    }
}
