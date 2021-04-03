package network;

import com.google.common.eventbus.Subscribe;

public abstract class Participant {

    private final String name;

    public Participant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void sendTransmission(IChannel channel, Transmission transmission) {
        channel.startTransmission(transmission);
    }

    @Subscribe
    public abstract void receive(Transmission transmission);
}
