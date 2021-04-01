package core.encryption;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

import java.io.File;

public class Encryption {
    private static BaseFactory baseFactory = new BaseFactory();
    private static CrackerFactory crackerFactory = new CrackerFactory();

    public static String encrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = baseFactory.build(algorithm);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            return (String) encryptPort.getClass()
                    .getMethod("encrypt", String.class, File.class)
                    .invoke(encryptPort, message, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while encrypting";
        }
    }

    public static String decrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = baseFactory.build(algorithm);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            return (String) encryptPort.getClass()
                    .getMethod("decrypt", String.class, File.class)
                    .invoke(encryptPort, message, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while encrypting";
        }
    }

    public static String crackShift(String message) {
        Object crackerPort = crackerFactory.build(EncryptionAlgorithm.SHIFT);

        try {
            return (String) crackerPort.getClass()
                    .getMethod("decrypt", String.class)
                    .invoke(crackerPort, message.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while cracking";
        }
    }

    public static String crackRSA(String message, String keyFilename) {
        Object crackerPort = crackerFactory.build(EncryptionAlgorithm.RSA);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            String crackedMessage = (String) crackerPort.getClass()
                    .getMethod("decrypt", String.class, File.class)
                    .invoke(crackerPort, message, keyFile);
            return (crackedMessage != null) ? crackedMessage : "cracking encrypted message \"" + message + "\" failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while cracking";
        }
    }
}
