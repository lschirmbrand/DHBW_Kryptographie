package core.encryption;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

public class CrackerFactory extends JarFactory {
    @Override
    String getClassName(EncryptionAlgorithm algorithm) {
        return switch (algorithm) {
            case SHIFT -> "ShiftCracker";
            case RSA -> "RSACracker";
        };
    }

    @Override
    String getArchivePath(EncryptionAlgorithm algorithm) {
        return switch (algorithm) {
            case SHIFT -> Configuration.instance.pathToShiftCrackerJavaArchive;
            case RSA -> Configuration.instance.pathToRSACrackerJavaArchive;
        };
    }
}
