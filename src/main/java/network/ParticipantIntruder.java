package network;

import configuration.Configuration;
import core.encryption.Encryption;
import core.encryption.RSACrackingException;

public class ParticipantIntruder extends Participant {
    public ParticipantIntruder(String name) {
        super(name);
    }

    @Override
    public void receive(Transmission transmission) {
        if (!transmission.getSender().equals(getName())) {
            try {
                String decrypted = switch (transmission.getAlgorithm()) {
                    case RSA -> Encryption.crackRSA(transmission.getMessage(), transmission.getKeyFilePath());
                    case SHIFT -> Encryption.crackShift(transmission.getMessage());
                };

                Configuration.instance.guiLogger.log("intruder " + getName() + " cracked message from participant " + transmission.getSender() + " | " + decrypted);

            } catch (RSACrackingException e) {
                Configuration.instance.guiLogger.log("intruder " + getName() + " | crack message from participant " + transmission.getSender() + " failed");
            }
        }
    }
}
