package core;

import configuration.EncryptionAlgorithm;
import core.encryption.Encryption;
import core.encryption.RSACrackingException;
import database.HSQLDBService;
import database.IDBService;
import database.models.Channel;
import database.models.Message;
import network.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityAgency {

    private final CommandInterpreter interpreter;

    private final IDBService dbService;

//    private final Map<String, Participant> participants;
//    private final Map<String, EventBus> channels;

    private final INetwork network;

    public SecurityAgency() {
        interpreter = new CommandInterpreter(this);
        dbService = HSQLDBService.instance;
        dbService.setup();

        network = new Network();

//        participants = new HashMap<>();
//        channels = new HashMap<>();
        setupParticipants();
        setupChannels();
    }

    private void setupParticipants() {
        for (database.models.Participant p : dbService.getParticipants()) {
            network.addParticipant(p.getName(), p.getType());
        }
    }

    private void setupChannels() {
        for (Channel channel : dbService.getChannels()) {

            Participant pA = network.getParticipant(channel.getParticipantA().getName());
            Participant pB = network.getParticipant(channel.getParticipantB().getName());

            network.addChannel(channel.getName(), pA, pB);
        }
    }

    public String encrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        return Encryption.encrypt(message, algorithm, keyFilename);
    }

    public String decrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        return Encryption.decrypt(message, algorithm, keyFilename);
    }

    public String crackShift(String message) {
        return Encryption.crackShift(message);
    }

    public String crackRSA(String message, String keyFilename) {
        try {
            return Encryption.crackRSA(message, keyFilename);
        } catch (RSACrackingException e) {
            return "cracking encryped message \"" + message + "\" failed";
        }
    }

    public String registerParticipant(String name, String type) {
        if (network.getParticipant(name) != null) {
            return "participant " + name + " already exists, using existing postbox_" + name;
        }

        dbService.insertParticipant(name, type);
        network.addParticipant(name, type);

        return "participant " + name + " with type " + type + " registered and postbox_" + name + " created";
    }

    public String createChannel(String name, String pName1, String pName2) {
        if (network.getChannel(name) != null) {
            return "channel " + name + " already exists";
        }

        if (pName1.equals(pName2)) {
            return pName1 + " and " + pName2 + " are identical - cannot create channel on itself";
        }

        List<IChannel> channels = network.getChannels();

        Participant a = network.getParticipant(pName1);
        if (a == null) {
            return pName1 + " is not a known participant";
        }
        Participant b = network.getParticipant(pName2);
        if (b == null) {
            return pName2 + " is not a known participant";
        }

        for (IChannel channel : channels) {
            List<Participant> participants = channel.getParticipants();
            if (participants.contains(a) && participants.contains(b)) {
                return "communication channel between " + pName1 + " and " + pName2 + " already exists";
            }
        }

        dbService.insertChannel(name, pName1, pName2);
        network.addChannel(name, a, b);

        return "channel " + name + " from " + pName1 + " to " + pName2 + " successfully created";
    }

    public String showChannel() {
        return dbService.getChannels().stream()
                .map(channel ->
                        channel.getName() + "\t | " + channel.getParticipantA().getName() + "\t and " + channel.getParticipantB().getName()
                ).collect(Collectors.joining("\n"));
    }

    public String dropChannel(String name) {
        List<String> channelNames = dbService.getChannels().stream().map(database.models.Channel::getName).collect(Collectors.toList());
        if (channelNames.contains(name)) {
            dbService.dropChannel(name);
            network.removeChannel(name);

            return "channel " + name + " deleted";
        } else {
            return "unknown channel " + name;
        }
    }

    public String intrudeChannel(String name, String part) {
        IChannel channel = network.getChannel(name);
        if (channel == null) {
            return "unknown Channel " + name;
        }

        Participant participant = network.getParticipant(part);
        if (!(participant instanceof ParticipantIntruder)) {
            return "participant " + name + " is not of type intruder";
        }

        network.intrudeChannel(name, (ParticipantIntruder) participant);
        return "participant " + part + " intruded channel " + name;
    }

    public String sendMessage(String message, String pName1, String pName2, EncryptionAlgorithm algorithm, String
            keyfileName) {

        Participant a = network.getParticipant(pName1);
        if (a == null) {
            return pName1 + " is not a known participant";
        }
        Participant b = network.getParticipant(pName2);
        if (b == null) {
            return pName2 + " is not a known participant";
        }

        IChannel channel = null;

        for (IChannel networkChannel : network.getChannels()) {
            List<Participant> participants = networkChannel.getParticipants();
            if (participants.contains(a) && participants.contains(b)) {
                channel = networkChannel;
                break;
            }
        }
        if (channel == null) {
            return "no valid channel from " + pName1 + " to " + pName2;
        }

        String encryptedMessage = Encryption.encrypt(message, algorithm, keyfileName);

        a.sendTransmission(channel, new Transmission(a.getName(), b.getName(), encryptedMessage, algorithm, keyfileName));

        Message dbMessage = new Message(a.getName(), b.getName(), algorithm.name().toLowerCase(), keyfileName, new Date().toString(), message, encryptedMessage);

        dbService.insertMessage(dbMessage);

        return "";
    }


    public CommandInterpreter getInterpreter() {
        return interpreter;
    }

}
