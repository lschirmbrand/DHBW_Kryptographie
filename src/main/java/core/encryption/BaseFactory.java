package core.encryption;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

public class BaseFactory extends JarFactory {

    protected String getClassName(EncryptionAlgorithm algorithm) {
        return switch (algorithm) {
            case SHIFT -> "ShiftBase";
            case RSA -> "RSABase";
        };
    }

    public String getArchivePath(EncryptionAlgorithm algorithm) {
        return switch (algorithm) {
            case SHIFT -> Configuration.instance.pathToShiftBaseJavaArchive;
            case RSA -> Configuration.instance.pathToRSABaseJavaArchive;
        };
    }
}
