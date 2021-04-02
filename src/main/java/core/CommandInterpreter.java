package core;

import configuration.EncryptionAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter {
    private final SecurityAgency securityAgency;
    Map<String, Function<List<String>, String>> regexMap;

    public CommandInterpreter(SecurityAgency securityAgency) {
        this.securityAgency = securityAgency;
        regexMap = Map.of(
                "encrypt message \"(.*)\" using (rsa|shift) and keyfile ([A-Za-z0-9]*.json)", this::encryptMessage,
                "decrypt message \"(.*)\" using (rsa|shift) and keyfile ([A-Za-z0-9]*.json)", this::decryptMessage,
                "crack encrypted message \"(.*)\" using (shift)", this::crackShift,
                "crack encrypted message \"(.*)\" using (rsa) and keyfile ([A-Za-z0-9]*.json)", this::crackRSA,
                "register participant (.*) with type (normal|intruder)", this::registerParticipant,
                "create channel (.*) from (.*) to (.*)", this::createChannel,
                "show channel", this::showChannel,
                "drop channel (.*)", this::dropChannel,
                "intrude channel (.*) by (.*)", this::intrudeChannel,
                "send message \"(.*)\" from (.*) to (.*) using (rsa|shift) and keyfile ([A-Za-z0-9]*.json)", this::sendMessage
        );
    }

    public String interpret(String command) {

        for (Map.Entry<String, Function<List<String>, String>> entry : regexMap.entrySet()) {
            List<String> groups = match(entry.getKey(), command);
            if (groups != null) {
                return entry.getValue().apply(groups);
            }
        }

        return "unknown command";
    }

    private List<String> match(String regex, String command) {
        List<String> matchedGroups = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            for (int i = 1; i < matcher.groupCount() + 1; i++) {
                matchedGroups.add(matcher.group(i));
            }
            return matchedGroups;
        }
        return null;
    }

    private String encryptMessage(List<String> groups) {
        String message = groups.get(0);
        EncryptionAlgorithm algorithm = getAlgorithm(groups.get(1));
        String keyFilename = groups.get(2);
        return this.securityAgency.encrypt(message, algorithm, keyFilename);
    }

    private String decryptMessage(List<String> groups) {
        String message = groups.get(0);
        EncryptionAlgorithm algorithm = getAlgorithm(groups.get(1));
        String keyFilename = groups.get(2);
        return this.securityAgency.decrypt(message, algorithm, keyFilename);
    }

    private String crackShift(List<String> groups) {
        String message = groups.get(0);
        return this.securityAgency.crackShift(message);
    }

    private String crackRSA(List<String> groups) {
        String message = groups.get(0);
        String keyFilename = groups.get(2);
        return this.securityAgency.crackRSA(message, keyFilename);
    }

    private String registerParticipant(List<String> groups) {
        String name = groups.get(0);
        String type = groups.get(1);
        Participant.Type participantType = switch (type) {
            case "normal" -> Participant.Type.NORMAL;
            case "intruder" -> Participant.Type.INTRUDER;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        return this.securityAgency.registerParticipant(name, participantType);
    }

    private String createChannel(List<String> groups) {
        String name = groups.get(0);
        String pName1 = groups.get(1);
        String pName2 = groups.get(2);
        return this.securityAgency.createChannel(name, pName1, pName2);
    }

    private String showChannel(List<String> groups) {
        return this.securityAgency.showChannel();
    }

    private String dropChannel(List<String> groups) {
        String name = groups.get(0);
        return securityAgency.dropChannel(name);
    }

    private String intrudeChannel(List<String> groups) {
        String name = groups.get(0);
        String part = groups.get(1);
        return securityAgency.intrudeChannel(name, part);
    }

    private String sendMessage(List<String> groups) {
        String message = groups.get(0);
        String pName1 = groups.get(1);
        String pName2 = groups.get(2);
        EncryptionAlgorithm algorithm = getAlgorithm(groups.get(3));
        String keyfileName = groups.get(4);
        return securityAgency.sendMessage(message, pName1, pName2, algorithm, keyfileName);
    }

    private EncryptionAlgorithm getAlgorithm(String arg) {
        return switch (arg.toLowerCase()) {
            case "shift" -> EncryptionAlgorithm.SHIFT;
            case "rsa" -> EncryptionAlgorithm.RSA;
            default -> throw new IllegalStateException("Unexpected value: " + arg);
        };
    }
}
