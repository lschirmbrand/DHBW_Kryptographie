package core.participant;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import configuration.EncryptionAlgorithm;
import core.ChannelEvent;
import core.encryption.Encryption;

import java.util.HashMap;
import java.util.Map;

public abstract class Participant {
    protected final String name;
    protected final Type type;


    private final Map<String, EventBus> channels = new HashMap<>();

    public Participant(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public void addChanel(String name, EventBus channel) {
        channels.put(name, channel);
        channel.register(this);
    }

    public void removeChanel(String name) {
        if (channels.containsKey(name)) {
            EventBus channel = channels.get(name);
            channels.remove(name);
            channel.unregister(this);
        }
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String sendMessage(String message, String name, EncryptionAlgorithm algorithm, String keyfileName) {
        String encrypted = Encryption.encrypt(message, algorithm, keyfileName);
        channels.get(name).post(new ChannelEvent(encrypted, algorithm, keyfileName, this));
        return encrypted;
    }

    @Subscribe
    public abstract void receive(ChannelEvent event);

    public enum Type {
        NORMAL("normal"), INTRUDER("intruder");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
