package edus2.application;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.application.exception.AuthenticationDisabledException;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;

import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticationFacadeTest {

    private InMemoryEDUS2Configuration configuration;
    private AuthenticationFacade authenticationFacade;

    @Before
    public void setup() {
        configuration = new InMemoryEDUS2Configuration();
        authenticationFacade = new AuthenticationFacade(configuration);
    }

    @Test
    public void isAuthenticationEnabled_shouldReturnFalse_whenAuthenticationDisabled() {
        // Arrange
        configuration.setHashedPassword(null);

        // Act
        boolean actual = authenticationFacade.isAuthenticationEnabled();

        // Assert
        assertFalse(actual);
    }

    @Test
    public void isAuthenticationEnabled_shouldReturnTrue_whenAuthenticationEnabled() {
        // Arrange
        configuration.setHashedPassword("hashed-password");

        // Act
        boolean actual = authenticationFacade.isAuthenticationEnabled();

        // Assert
        assertTrue(actual);
    }

    @Test (expected = AuthenticationDisabledException.class)
    public void isValidLogin_shouldThrowException_whenAuthenticationDisabled() {
        // Arrange
        configuration.setHashedPassword(null);

        // Act
        authenticationFacade.isValidLogin(randomAlphanumericString());
    }

    @Test
    public void isValidLogin_shouldReturnFalse_whenGivenInvalidLogin() {
        // Arrange
        configuration.setHashedPassword(DigestUtils.sha256Hex("password"));

        // Act
        boolean actual = authenticationFacade.isValidLogin("incorrect password");

        // Assert
        assertFalse(actual);
    }

    @Test
    public void isValidLogin_shouldReturnTrue_whenGivenValidLogin() {
        // Arrange
        configuration.setHashedPassword(DigestUtils.sha256Hex("password"));

        // Act
        boolean actual = authenticationFacade.isValidLogin("password");

        // Assert
        assertTrue(actual);
    }

    @Test
    public void setPassword_shouldSetPassword() {
        // Arrange
        authenticationFacade.setPassword("password");

        // Act
        boolean actual = authenticationFacade.isValidLogin("password");

        // Assert
        assertTrue(actual);
    }
}