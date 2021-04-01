package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;
import database.models.Participant;

import java.io.File;

public class SecurityAgency {

    private final CommandInterpreter interpreter = new CommandInterpreter(this);

    private final BaseFactory baseFactory = new BaseFactory();
    private final CrackerFactory crackerFactory = new CrackerFactory();

    public CommandInterpreter getInterpreter() {
        return interpreter;
    }

    public String encrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
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

    public String decrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
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

    public String crackShift(String message) {
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

    public String crackRSA(String message, String keyFilename) {
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

    public void registerParticipant(String name, Participant.Type type) {
    }

    public void createChannel(String name, String part1, String part2) {
    }

    public void showChannel() {
    }

    public void dropChannel(String name) {
    }

    public void intrudeChannel(String name, String part) {
    }

    public void sendMessage(String message, String part1, String part2, EncryptionAlgorithm algorithm, String keyfileName) {
    }

}
