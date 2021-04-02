package core.participant;

import configuration.Configuration;
import core.ChannelEvent;
import core.encryption.Encryption;
import core.encryption.RSACrackingException;

public class IntruderParticipant extends Participant {
    public IntruderParticipant(String name, Type type) {
        super(name, type);
    }

    @Override
    public void receive(ChannelEvent event) {
        if (!event.getSender().equals(this)) {
            try {
                String decrypted = switch (event.getAlgorithm()) {
                    case RSA -> Encryption.crackRSA(event.getMessage(), event.getKeyfile());
                    case SHIFT -> Encryption.crackShift(event.getMessage());
                };

                Configuration.instance.guiLogger.log("intruder " + name + " cracked message from participant " + event.getSender().getName() + " | " + decrypted);

            } catch (RSACrackingException e) {
                Configuration.instance.guiLogger.log("intruder " + name + " | crack message from participant " + event.getSender().getName() + " failed");
            }
        }
    }
}
