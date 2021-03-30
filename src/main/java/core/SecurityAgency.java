package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;
import database.models.Participant;

import java.io.File;

public class SecurityAgency {

    private final CommandInterpreter interpreter;

    public SecurityAgency() {
        this.interpreter = new CommandInterpreter(this);
    }

    public CommandInterpreter getInterpreter() {
        return interpreter;
    }

    public String encrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = EncryptFactory.build(algorithm);

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
        Object encryptPort = EncryptFactory.build(algorithm);

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

    public void crackShift(String message) {
    }

    public void crackRSA(String message, String keyFilename) {
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
