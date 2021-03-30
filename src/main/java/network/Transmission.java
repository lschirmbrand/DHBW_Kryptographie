package network;

import configuration.AlgorithmType;

public class Transmission {
    private final String sender;
    private final String receiver;
    private final String message;
    private final AlgorithmType algorithm;
    private final String keyFilePath;

    public Transmission(String sender, String receiver, String message, AlgorithmType algorithm, String keyFilePath) {
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

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

}
