package network;

import com.google.common.eventbus.Subscribe;

public abstract class Participant {

    private final String name;

    public Participant(String name) {
        this.name = name;
    }

    public boolean isTransmissionReceiver(Transmission transmission) {
        return this.name.equals(transmission.getReceiver());
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
