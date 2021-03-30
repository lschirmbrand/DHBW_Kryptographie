package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class BaseFactory {
    public static Object build(EncryptionAlgorithm algorithm) {
        Object port = null;

        String archivePath = switch (algorithm) {
            case SHIFT -> Configuration.instance.pathToShiftBaseJavaArchive;
            case RSA -> Configuration.instance.pathToRSABaseJavaArchive;
        };

        String className = switch (algorithm) {
            case SHIFT -> "ShiftBase";
            case RSA -> "RSABase";
        };

        try {
            URL[] urls = {new File(archivePath).toURI().toURL()};
            URLClassLoader urlClassLoader = new URLClassLoader(urls, BaseFactory.class.getClassLoader());
            Class clazz = Class.forName(className, true, urlClassLoader);
            Object instance = clazz.getMethod("getInstance").invoke(null);
            port = clazz.getDeclaredField("port").get(instance);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return port;
    }
}
