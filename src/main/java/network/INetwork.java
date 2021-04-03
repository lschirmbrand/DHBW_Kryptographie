package network;

import java.util.List;

public interface INetwork {
    void addChannel(String name, Participant a, Participant b);

    void removeChannel(String name);

    IChannel getChannel(String name);

    List<IChannel> getChannels();

    void addParticipant(String name, String type);

    Participant getParticipant(String name);

    void intrudeChannel(String name, ParticipantIntruder intruder);
}
