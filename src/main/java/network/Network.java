package network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network implements INetwork{

    private Map<String, IChannel> channels = new HashMap<>();

    @Override
    public void createChannel(String name, Participant a, Participant b) {
        IChannel channel = new Channel(name);
        channel.addParticipant(a);
        channel.addParticipant(b);
        this.channels.put(name, channel);
    }

    @Override
    public void removeChannel(String name) {
        this.channels.remove(name);
    }

    @Override
    public void addIntruderToChannel(String name, Participant intruder) {
        this.channels.get(name).addParticipant(intruder);
    }

    @Override
    public void sendTransmission(String name, Transmission transmission) {
        this.channels.get(name).startTransmission(transmission);
    }

    @Override
    public Participant getParticipant(String name) {
        for(IChannel channel : this.channels.values()){
            List<Participant> participants = channel.getParticipants();

            for(Participant participant : participants){
                if(participant.getName().equals(name))
                    return participant;
            }
        }
        return null;
    }
}
