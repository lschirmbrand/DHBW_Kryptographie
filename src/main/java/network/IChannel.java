package network;

import java.util.List;

public interface IChannel {
    String getName();

    void startTransmission(Transmission transmission);

    void intrude(ParticipantIntruder intruder);

    List<Participant> getParticipants();
}
