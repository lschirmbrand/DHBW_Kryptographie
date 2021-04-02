package core.participant;


import configuration.Configuration;
import core.ChannelEvent;
import core.encryption.Encryption;

public class NormalParticipant extends Participant {
    public NormalParticipant(String name, Type type) {
        super(name, type);
    }

    @Override
    public void receive(ChannelEvent event) {
        if (!event.getSender().equals(this)) {
            String decrypted = Encryption.decrypt(event.getMessage(), event.getAlgorithm(), event.getKeyfile());
            Configuration.instance.guiLogger.log(name + " received new message: " + decrypted);
        }
    }
}
