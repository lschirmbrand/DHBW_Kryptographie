package network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network implements INetwork {

    private final Map<String, IChannel> channels = new HashMap<>();
    private final Map<String, Participant> participants = new HashMap<>();

    @Override
    public void addChannel(String name, Participant a, Participant b) {
        IChannel channel = new Channel(name, a, b);
        this.channels.put(name, channel);
    }

    @Override
    public void removeChannel(String name) {
        this.channels.remove(name);
    }

    @Override
    public IChannel getChannel(String name) {
        return channels.get(name);
    }

    @Override
    public List<IChannel> getChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public void addParticipant(String name, String type) {
        Participant participant = switch (type) {
            case "normal" -> new ParticipantDefault(name);
            case "intruder" -> new ParticipantIntruder(name);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        participants.put(name, participant);
    }

    @Override
    public void intrudeChannel(String name, ParticipantIntruder intruder) {
        this.channels.get(name).intrude(intruder);
    }


    @Override
    public Participant getParticipant(String name) {
        return participants.get(name);
    }
}
