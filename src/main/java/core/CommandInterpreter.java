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

    private String encryptMessage(List<String> groups) {
        return this.securityAgency.encrypt(groups.get(0), getAlgorithm(groups.get(1)), groups.get(2));
    }

    private String decryptMessage(List<String> groups) {
        return this.securityAgency.decrypt(groups.get(0), getAlgorithm(groups.get(1)), groups.get(2));
    }

    private String crackShift(List<String> groups) {
        return this.securityAgency.crackShift(groups.get(0));
    }

    private String crackRSA(List<String> groups) {
        return this.securityAgency.crackRSA(groups.get(0), groups.get(2));
    }

    private String registerParticipant(List<String> groups) {
        return this.securityAgency.registerParticipant(groups.get(0), getParticipantType(groups.get(1)));
    }

    private String createChannel(List<String> groups) {
        return this.securityAgency.createChannel(groups.get(0), groups.get(1), groups.get(2));
    }

    private String showChannel(List<String> groups) {
        return this.securityAgency.showChannel();
    }

    private String dropChannel(List<String> groups) {
        return securityAgency.dropChannel(groups.get(0));
    }

    private String intrudeChannel(List<String> groups) {
        return securityAgency.intrudeChannel(groups.get(0), groups.get(1));
    }

    private String sendMessage(List<String> groups) {
        return securityAgency.sendMessage(groups.get(0), groups.get(1), groups.get(2), getAlgorithm(groups.get(3)), groups.get(4));
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

    private EncryptionAlgorithm getAlgorithm(String arg) {
        return switch (arg.toLowerCase()) {
            case "shift" -> EncryptionAlgorithm.SHIFT;
            case "rsa" -> EncryptionAlgorithm.RSA;
            default -> throw new IllegalStateException("Unexpected value: " + arg);
        };
    }

    private Participant.Type getParticipantType(String type) {
        return switch (type) {
            case "normal" -> Participant.Type.NORMAL;
            case "intruder" -> Participant.Type.INTRUDER;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
