package network;

import com.google.common.eventbus.Subscribe;

public class ParticipantIntruder extends Participant{
    public ParticipantIntruder(String name) {
        super(name);
    }

    @Subscribe
    @Override
    public void receive(Transmission transmission){
        // Intruder way of receiving
    }
}
