package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterpreter {
    private final SecurityAgency securityAgency;
    Map<String, Consumer<List<String>>> regexMap;

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

    public void interpret(String command) {

        Configuration.instance.guiLogger.log("-------------------------------");


        for (Map.Entry<String, Consumer<List<String>>> entry : regexMap.entrySet()) {
            List<String> groups = match(entry.getKey(), command);
            if (groups != null) {
                entry.getValue().accept(groups);
                return;
            }
        }

        Configuration.instance.guiLogger.log("unknown command");
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

    private void encryptMessage(List<String> groups) {
        String message = groups.get(0);
        EncryptionAlgorithm algorithm = getAlgorithm(groups.get(1));
        String keyFilename = groups.get(2);
        Configuration.instance.guiLogger.log(this.securityAgency.encrypt(message, algorithm, keyFilename));
    }

    private void decryptMessage(List<String> groups) {
        String message = groups.get(0);
        EncryptionAlgorithm algorithm = getAlgorithm(groups.get(1));
        String keyFilename = groups.get(2);
        Configuration.instance.guiLogger.log(this.securityAgency.decrypt(message, algorithm, keyFilename));
    }

    private void crackShift(List<String> groups) {
        String message = groups.get(0);
        Configuration.instance.guiLogger.log(this.securityAgency.crackShift(message));
    }

    private void crackRSA(List<String> groups) {
        String message = groups.get(0);
        String keyFilename = groups.get(2);
        Configuration.instance.guiLogger.log(this.securityAgency.crackRSA(message, keyFilename));
    }

    private void registerParticipant(List<String> groups) {
        String name = groups.get(0);
        String type = groups.get(1);

        Configuration.instance.guiLogger.log(this.securityAgency.registerParticipant(name, type));
    }

    private void createChannel(List<String> groups) {
        String name = groups.get(0);
        String pName1 = groups.get(1);
        String pName2 = groups.get(2);
        Configuration.instance.guiLogger.log(this.securityAgency.createChannel(name, pName1, pName2));
    }

    private void showChannel(List<String> groups) {
        Configuration.instance.guiLogger.log(this.securityAgency.showChannel());
    }

    private void dropChannel(List<String> groups) {
        String name = groups.get(0);
        Configuration.instance.guiLogger.log(securityAgency.dropChannel(name));
    }

    private void intrudeChannel(List<String> groups) {
        String name = groups.get(0);
        String part = groups.get(1);
        Configuration.instance.guiLogger.log(securityAgency.intrudeChannel(name, part));
    }

    private void sendMessage(List<String> groups) {
        String message = groups.get(0);
        String pName1 = groups.get(1);
        String pName2 = groups.get(2);
        EncryptionAlgorithm algorithm = getAlgorithm(groups.get(3));
        String keyfileName = groups.get(4);
        Configuration.instance.guiLogger.log(securityAgency.sendMessage(message, pName1, pName2, algorithm, keyfileName));
    }

    private EncryptionAlgorithm getAlgorithm(String arg) {
        return switch (arg.toLowerCase()) {
            case "shift" -> EncryptionAlgorithm.SHIFT;
            case "rsa" -> EncryptionAlgorithm.RSA;
            default -> throw new IllegalStateException("Unexpected value: " + arg);
        };
    }
}
