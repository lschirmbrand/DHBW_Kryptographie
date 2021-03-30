package network;

import configuration.EncryptionAlgorithm;

public class Transmission {
    private final String sender;
    private final String receiver;
    private final String message;
    private final EncryptionAlgorithm algorithm;
    private final String keyFilePath;

    public Transmission(String sender, String receiver, String message, EncryptionAlgorithm algorithm, String keyFilePath) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.algorithm = algorithm;
        this.keyFilePath = keyFilePath;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public EncryptionAlgorithm getAlgorithm() {
        return algorithm;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

}
