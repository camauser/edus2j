package edus2.application;

import edus2.application.exception.AuthenticationDisabledException;
import edus2.domain.EDUS2Configuration;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;

public class AuthenticationFacade {
    private EDUS2Configuration configuration;

    @Inject
    public AuthenticationFacade(EDUS2Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean isAuthenticationEnabled() {
        return configuration.getHashedPassword().isPresent();
    }

    public boolean isValidLogin(String attemptedPassword) {
        if (!configuration.getHashedPassword().isPresent()) {
            throw new AuthenticationDisabledException("Password authentication is disabled, can't check for valid login.");
        }

        String hashedPassword = configuration.getHashedPassword().get();
        String hashedAttempt = DigestUtils.sha256Hex(attemptedPassword);

        return hashedAttempt.equalsIgnoreCase(hashedPassword);
    }

    public void setPassword(String newPassword) {
        configuration.setHashedPassword(DigestUtils.sha256Hex(newPassword));
    }

    public void clearPassword() {
        configuration.setHashedPassword(null);
    }
}
