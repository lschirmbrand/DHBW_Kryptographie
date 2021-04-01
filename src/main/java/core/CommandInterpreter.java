package core;

import configuration.EncryptionAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter {
    Map<String, Function<List<String>, String>> regexMap;

    public CommandInterpreter(SecurityAgency securityAgency) {

        regexMap = Map.of(
                "encrypt message \"(.*)\" using (rsa|shift) and keyfile ([A-Za-z0-9]*.json)",
                groups -> securityAgency.encrypt(groups.get(0), getAlgorithm(groups.get(1)), groups.get(2)),
                "decrypt message \"(.*)\" using (rsa|shift) and keyfile ([A-Za-z0-9]*.json)",
                groups -> securityAgency.decrypt(groups.get(0), getAlgorithm(groups.get(1)), groups.get(2)),
                "crack encrypted message \"(.*)\" using (shift)",
                groups -> securityAgency.crackShift(groups.get(0)),
                "crack encrypted message \"(.*)\" using (rsa) and keyfile ([A-Za-z0-9]*.json)",
                groups -> securityAgency.crackRSA(groups.get(0), groups.get(2)),
                "register participant (.*) with type (normal|intruder)",
                groups -> securityAgency.registerParticipant(groups.get(0), getParticipantType(groups.get(1))),
                "create channel (.*) from (.*) to (.*)",
                groups -> securityAgency.createChannel(groups.get(0), groups.get(1), groups.get(2)),
                "show channel",
                groups -> securityAgency.showChannel(),
                "drop channel (.*)",
                groups -> securityAgency.dropChannel(groups.get(0)),
                "intrude channel (.*) by (.*)",
                groups -> securityAgency.intrudeChannel(groups.get(0), groups.get(1)),
                "send message \"(.*)\" from (.*) to (.*) using (rsa|shift) and keyfile ([A-Za-z0-9]*.json)",
                groups -> securityAgency.sendMessage(groups.get(0), groups.get(1), groups.get(2), getAlgorithm(groups.get(3)), groups.get(4))
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
