package network;

import java.util.List;

public interface IChannel {
    void startTransmission(Transmission transmission);

    void addParticipant(Participant participant);

    void addToEventBuss(Participant participant);

    List<Participant> getParticipants();
}
