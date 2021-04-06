package core.encryption;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class Encryption {
    private static final BaseFactory baseFactory = new BaseFactory();
    private static final CrackerFactory crackerFactory = new CrackerFactory();
    private static final PrintStream consoleOut = System.out;

    public static String encrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = baseFactory.build(algorithm);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        switchSystemOut(false, algorithm);

        String res;
        try {
            res = (String) encryptPort.getClass()
                    .getMethod("encrypt", String.class, File.class)
                    .invoke(encryptPort, message, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
            res = "Error while encrypting";
        }

        restoreSystemOut();
        return res;
    }

    public static String decrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = baseFactory.build(algorithm);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);
        if (Configuration.instance.debugMode) {
            switchSystemOut(true, algorithm);
        }
        String res;
        try {
            res = (String) encryptPort.getClass()
                    .getMethod("decrypt", String.class, File.class)
                    .invoke(encryptPort, message, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
            res = "Error while encrypting";
        }
        if (Configuration.instance.debugMode) {
            restoreSystemOut();
        }
        return res;
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

    public static String crackRSA(String message, String keyFilename) throws RSACrackingException {
        Object crackerPort = crackerFactory.build(EncryptionAlgorithm.RSA);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            String crackedMessage = (String) crackerPort.getClass()
                    .getMethod("decrypt", String.class, File.class)
                    .invoke(crackerPort, message, keyFile);
            return (crackedMessage != null) ? crackedMessage : "cracking encrypted message \"" + message + "\" failed";
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RSACrackingException();
        }
    }


    private static void switchSystemOut(boolean decrypt, EncryptionAlgorithm algorithm) {
        if (Configuration.instance.debugMode) {
            File logDir = new File(Configuration.instance.logsDirectory);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            String fileName = Configuration.instance.logsDirectory + (decrypt ? "decrypt_" : "encrypt_") + algorithm.name().toLowerCase() + "_" + new Date().getTime() + ".txt";
            try {
                System.setOut(new PrintStream(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // suppress logging if not in debug mode
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            }));
        }
    }

    private static void restoreSystemOut() {
        System.setOut(consoleOut);
    }
}
