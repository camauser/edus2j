package edus2.application;

/**
 * Separate entry point so the packaged jar can start when JavaFX is on the
 * classpath rather than the module path (required since JavaFX left the JDK).
 */
public class Launcher {
    public static void main(String[] args) {
        EDUS2View.main(args);
    }
}
