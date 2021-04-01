package core;

import configuration.EncryptionAlgorithm;

public class ChannelEvent {
    private final String message;
    private final EncryptionAlgorithm algorithm;
    private final String keyfile;
    private final Participant sender;

    public ChannelEvent(String message, EncryptionAlgorithm algorithm, String keyfile, Participant sender) {
        this.message = message;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public EncryptionAlgorithm getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }

    public Participant getSender() {
        return sender;
    }
}
