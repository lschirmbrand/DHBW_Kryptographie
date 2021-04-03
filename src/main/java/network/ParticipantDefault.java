package network;

import configuration.Configuration;
import core.encryption.Encryption;

public class ParticipantDefault extends Participant {
    public ParticipantDefault(String name) {
        super(name);
    }

    @Override
    public void receive(Transmission transmission) {
        if (!isTransmissionReceiver(transmission)) {
            String decrypted = Encryption.decrypt(transmission.getMessage(), transmission.getAlgorithm(), transmission.getKeyFilePath());
            Configuration.instance.guiLogger.log(getName() + " received new message: " + decrypted);
        }
    }
}
