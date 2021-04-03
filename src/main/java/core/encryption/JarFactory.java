package core.encryption;

import configuration.EncryptionAlgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

public abstract class JarFactory {
    protected boolean verify(String pathToJar) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("jarsigner", "-verify", pathToJar);
            Process process = processBuilder.start();
            process.waitFor();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("verified")) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Object build(EncryptionAlgorithm algorithm) {
        Object port = null;

        String archivePath = getArchivePath(algorithm);

        String className = getClassName(algorithm);

        if (verify(archivePath)) {

            try {
                URL[] urls = {new File(archivePath).toURI().toURL()};
                URLClassLoader urlClassLoader = new URLClassLoader(urls, BaseFactory.class.getClassLoader());
                Class<?> clazz = Class.forName(className, true, urlClassLoader);
                Object instance = clazz.getMethod("getInstance").invoke(null);
                port = clazz.getDeclaredField("port").get(instance);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return port;
        } else throw new RuntimeException("Java-Archive not verified");
    }

    abstract String getClassName(EncryptionAlgorithm algorithm);

    abstract String getArchivePath(EncryptionAlgorithm algorithm);
}
