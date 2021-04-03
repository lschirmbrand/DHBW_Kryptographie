package network;

import com.google.common.eventbus.EventBus;

import java.util.List;

public class Channel implements IChannel {

    private final String name;
    private final EventBus eventBus;
    private final Participant participantA;
    private final Participant participantB;

    public Channel(String name, Participant participantA, Participant participantB) {
        this.name = name;
        this.participantA = participantA;
        this.participantB = participantB;
        this.eventBus = new EventBus();

        eventBus.register(participantA);
        eventBus.register(participantB);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void startTransmission(Transmission transmission) {
        this.eventBus.post(transmission);
    }

    @Override
    public void intrude(ParticipantIntruder intruder) {
        eventBus.register(intruder);
    }

    @Override
    public List<Participant> getParticipants() {
        return List.of(participantA, participantB);
    }


}
