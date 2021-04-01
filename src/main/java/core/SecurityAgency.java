package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;
import database.MSADBService;
import database.models.Channel;
import database.models.Participant;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityAgency {

    private final CommandInterpreter interpreter = new CommandInterpreter(this);

    private final BaseFactory baseFactory = new BaseFactory();
    private final CrackerFactory crackerFactory = new CrackerFactory();

    private final MSADBService msadbService = MSADBService.instance;

    public CommandInterpreter getInterpreter() {
        return interpreter;
    }

    public String encrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = baseFactory.build(algorithm);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            return (String) encryptPort.getClass()
                    .getMethod("encrypt", String.class, File.class)
                    .invoke(encryptPort, message, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while encrypting";
        }
    }

    public String decrypt(String message, EncryptionAlgorithm algorithm, String keyFilename) {
        Object encryptPort = baseFactory.build(algorithm);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            return (String) encryptPort.getClass()
                    .getMethod("decrypt", String.class, File.class)
                    .invoke(encryptPort, message, keyFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while encrypting";
        }
    }

    public String crackShift(String message) {
        Object crackerPort = crackerFactory.build(EncryptionAlgorithm.SHIFT);

        try {
            return (String) crackerPort.getClass()
                    .getMethod("decrypt", String.class)
                    .invoke(crackerPort, message.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while cracking";
        }
    }

    public String crackRSA(String message, String keyFilename) {
        Object crackerPort = crackerFactory.build(EncryptionAlgorithm.RSA);

        File keyFile = new File(Configuration.instance.commonPathToKeyFile + keyFilename);

        try {
            String crackedMessage = (String) crackerPort.getClass()
                    .getMethod("decrypt", String.class, File.class)
                    .invoke(crackerPort, message, keyFile);
            return (crackedMessage != null) ? crackedMessage : "cracking encrypted message \"" + message + "\" failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error while cracking";
        }
    }

    public String registerParticipant(String name, Participant.Type type) {
        if (!msadbService.getTypes().contains(type.getValue())) {
            msadbService.insertType(type.getValue());
        }

        if (msadbService.participantExists(name)) {
            return "participant " + name + " already exists, using existing postbox_" + name;
        } else {
            msadbService.insertParticipant(name, type.getValue());
            return "participant " + name + " with type " + type.getValue() + " registered and postbox_" + name + " created";
        }
    }

    public String createChannel(String name, String part1, String part2) {
        if (msadbService.channelExists(name)) {
            return "channel " + name + " already exists";
        }
        List<Channel> channels = msadbService.getChannels();
        for (Channel channel : channels) {
            if ((channel.getParticipantA().getName().equals(part1) && channel.getParticipantB().getName().equals(part2))
                    || (channel.getParticipantA().getName().equals(part2) && channel.getParticipantB().getName().equals(part1))) {
                return "communication channel between " + part1 + " and " + part2 + " already exists";
            }
        }
        if (part1.equals(part2)) {
            return part1 + " and " + part2 + " are identical - cannot create channel on itself";
        }
        List<String> partNames = msadbService.getParticipants().stream().map(Participant::getName).collect(Collectors.toList());
        if (!partNames.contains(part1)) {
            return part1 + " is not a known participant";
        }
        if (!partNames.contains(part2)) {
            return part2 + " is not a known participant";
        }

        msadbService.insertChannel(name, part1, part2);
        return "channel " + name + "from " + part1 + " to " + part2 + " successfully created";
    }

    public String showChannel() {
        return msadbService.getChannels().stream()
                .map(channel ->
                        channel.getName() + "\t | " + channel.getParticipantA().getName() + "\t and " + channel.getParticipantB().getName()
                ).collect(Collectors.joining("\n"));
    }

    public String dropChannel(String name) {
        List<String> channelNames = msadbService.getChannels().stream().map(Channel::getName).collect(Collectors.toList());
        if (channelNames.contains(name)) {
            msadbService.dropChannel(name);
            return "channel " + name + " deleted";
        } else {
            return "unknown channel " + name;
        }
    }

    public String intrudeChannel(String name, String part) {
        return "";
    }

    public String sendMessage(String message, String part1, String part2, EncryptionAlgorithm algorithm, String keyfileName) {
        return "";
    }

}
