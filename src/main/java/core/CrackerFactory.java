package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

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
