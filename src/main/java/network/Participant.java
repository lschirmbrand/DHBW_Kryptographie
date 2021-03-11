package network;

import com.google.common.eventbus.Subscribe;

public abstract class Participant {

    private String name;

    public Participant(String name){
        this.name = name;
    }

    public boolean isTransmissionReceiver(Transmission transmission){
        return this.name.equals(transmission.getReceiver());
    }

    public String getName(){
        return name;
    }

    @Subscribe
    public void receive(Transmission transmission){
        //Normal way of receiving
    }
}
