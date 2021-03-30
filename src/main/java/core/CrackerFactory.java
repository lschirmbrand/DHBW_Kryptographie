package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class CrackerFactory {
    public static Object build(EncryptionAlgorithm algorithm) {
        Object port = null;

        String archivePath = switch (algorithm) {
            case SHIFT -> Configuration.instance.pathToShiftCrackerJavaArchive;
            case RSA -> Configuration.instance.pathToRSACrackerJavaArchive;
        };

        String className = switch (algorithm) {
            case SHIFT -> "ShiftCracker";
            case RSA -> "RSACracker";
        };

        try {
            URL[] urls = {new File(archivePath).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, CrackerFactory.class.getClassLoader());
            Class clazz = Class.forName(className, true, urlClassLoader);
            Object instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return port;
    }
}
