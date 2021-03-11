package network;

public interface INetwork {
    void createChannel(String name, Participant a, Participant b);

    void removeChannel(String name);

    void addIntruderToChannel(String name, Participant intruder);

    void sendTransmission(String name, Transmission transmission);

    Participant getParticipant(String name);
}
