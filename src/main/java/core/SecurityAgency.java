package core;

import com.google.common.eventbus.EventBus;
import configuration.EncryptionAlgorithm;
import core.encryption.Encryption;
import database.HSQLDBService;
import database.IDBService;
import database.models.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecurityAgency {

    private final CommandInterpreter interpreter;

    private final IDBService dbService;

    private final Map<String, Participant> participants;
    private final Map<String, EventBus> channels;

    public SecurityAgency() {
        interpreter = new CommandInterpreter(this);
        dbService = HSQLDBService.instance;
        dbService.setupConnection();

        participants = new HashMap<>();
        channels = new HashMap<>();
        setupParticipants();
        setupChannels();
    }

    private void setupParticipants() {
        for (database.models.Participant participant : dbService.getParticipants()) {
            Participant.Type type = switch (participant.getType()) {
                case "normal" -> Participant.Type.NORMAL;
                case "intruder" -> Participant.Type.INTRUDER;
                default -> throw new IllegalStateException("Unexpected value: " + participant.getType());
            };
            participants.put(participant.getName(), new Participant(participant.getName(), type));
        }
    }

    private void setupChannels() {
        for (Channel channel : dbService.getChannels()) {
            addChannel(channel.getName(), channel.getParticipantA().getName(), channel.getParticipantB().getName());
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
        return Encryption.crackRSA(message, keyFilename);
    }

    public String registerParticipant(String name, Participant.Type type) {
        if (participants.containsKey(name)) {
            return "participant " + name + " already exists, using existing postbox_" + name;
        }
        if (!dbService.getTypes().contains(type.getValue())) {
            dbService.insertType(type.getValue());
        }

        dbService.insertParticipant(name, type.getValue());
        participants.put(name, new Participant(name, type));
        return "participant " + name + " with type " + type.getValue() + " registered and postbox_" + name + " created";
    }

    public String createChannel(String name, String pName1, String pName2) {
        if (channels.containsKey(name)) {
            return "channel " + name + " already exists";
        }
        List<database.models.Channel> dbChannels = dbService.getChannels();
        for (database.models.Channel channel : dbChannels) {
            if ((channel.getParticipantA().getName().equals(pName1) && channel.getParticipantB().getName().equals(pName2))
                    || (channel.getParticipantA().getName().equals(pName2) && channel.getParticipantB().getName().equals(pName1))) {
                return "communication channel between " + pName1 + " and " + pName2 + " already exists";
            }
        }
        if (pName1.equals(pName2)) {
            return pName1 + " and " + pName2 + " are identical - cannot create channel on itself";
        }
        List<String> partNames = dbService.getParticipants().stream().map(database.models.Participant::getName).collect(Collectors.toList());
        if (!partNames.contains(pName1)) {
            return pName1 + " is not a known participant";
        }
        if (!partNames.contains(pName2)) {
            return pName2 + " is not a known participant";
        }
        addChannel(name, pName1, pName2);

        return "channel " + name + "from " + pName1 + " to " + pName2 + " successfully created";
    }

    private void addChannel(String name, String pName1, String pName2) {
        dbService.insertChannel(name, pName1, pName2);

        Participant participantA = participants.get(pName1);
        Participant participantB = participants.get(pName2);

        EventBus channel = new EventBus(name);
        channels.put(name, channel);

        participantA.addChanel(name, channel);
        participantB.addChanel(name, channel);
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
            for (Participant participant : participants.values()) {
                participant.removeChanel(name);
            }
            return "channel " + name + " deleted";
        } else {
            return "unknown channel " + name;
        }
    }

    public String intrudeChannel(String name, String part) {
        if (!channels.containsKey(name)) {
            return "unknown Channel " + name;
        }
        Participant participant = participants.get(part);
        if (!(participant.getType() == Participant.Type.INTRUDER)) {
            return "participant " + name + " is not of type intruder";
        }

        participant.addChanel(name, channels.get(name));
        return "";
    }

    public String sendMessage(String message, String pName1, String pName2, EncryptionAlgorithm algorithm, String keyfileName) {

        Participant participantA = participants.get(pName1);
        Participant participantB = participants.get(pName2);

        List<Channel> channels = dbService.getChannels();
        Channel channel = null;
        for (Channel ch : channels) {
            if ((ch.getParticipantA().getName().equals(pName1) && ch.getParticipantB().getName().equals(pName2))
                    || (ch.getParticipantA().getName().equals(pName2) && ch.getParticipantB().getName().equals(pName1))) {
                channel = ch;
            }
        }
        if (channel == null) {
            return "no valid channel from " + pName1 + " to " + pName2;
        }

        participantA.sendMessage(message, channel.getName(), algorithm, keyfileName);


        return "";
    }


    public CommandInterpreter getInterpreter() {
        return interpreter;
    }

}
