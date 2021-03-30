package core;

import configuration.Configuration;
import configuration.EncryptionAlgorithm;
import database.models.Participant;

public class CommandInterpreter {
    private final SecurityAgency securityAgency;

    public CommandInterpreter(SecurityAgency securityAgency) {
        this.securityAgency = securityAgency;
    }

    public String interpret(String command) {

        String[] args = command.split(" |"+ Configuration.instance.lineSeparator);

        if (command.startsWith("encrypt message")) {
            String message = args[2].substring(1, args[2].length() - 1);
            EncryptionAlgorithm algorithm = getAlgorithm(args[4]);
            String keyFilename = args[7];

            return securityAgency.encrypt(message, algorithm, keyFilename);

        } else if (command.startsWith("decrypt message")) {
            String message = args[2].substring(1, args[2].length() - 1);
            EncryptionAlgorithm algorithm = getAlgorithm(args[4]);
            String keyFilename = args[7];

            return securityAgency.decrypt(message, algorithm, keyFilename);
        } else if (command.startsWith("crack encrypted message")) {
            String message = args[3].substring(1, args[2].length() - 1);
            EncryptionAlgorithm algorithm = getAlgorithm(args[5]);
            if (algorithm == EncryptionAlgorithm.SHIFT) {
                securityAgency.crackShift(message);
            } else {
                String keyFilename = args[8];
                securityAgency.crackRSA(message, keyFilename);
            }
        } else if (command.startsWith("register participant")) {
            String name = args[2];
            Participant.Type type = switch (args[5]) {
                case "normal" -> Participant.Type.NORMAL;
                case "intruder" -> Participant.Type.INTRUDER;
                default -> throw new IllegalStateException("Unexpected value: " + args[5]);
            };
            securityAgency.registerParticipant(name, type);
        } else if (command.startsWith("create channel")) {
            String name = args[2];
            String part1 = args[4];
            String part2 = args[6];
            securityAgency.createChannel(name, part1, part2);
        } else if (command.startsWith("show channel")) {
            securityAgency.showChannel();
        } else if (command.startsWith("drop channel")) {
            String name = args[2];
            securityAgency.dropChannel(name);
        } else if (command.startsWith("intrude channel")) {
            String name = args[2];
            String part = args[4];
            securityAgency.intrudeChannel(name, part);
        } else if (command.startsWith("send message")) {
            String message = args[2].substring(1, args[2].length() - 1);
            String part1 = args[4];
            String part2 = args[6];
            EncryptionAlgorithm algorithm = getAlgorithm(args[8]);
            String keyfileName = args[11];
            securityAgency.sendMessage(message, part1, part2, algorithm, keyfileName);
        }

        return "";
    }


    private EncryptionAlgorithm getAlgorithm(String arg) {
        return switch (arg.toLowerCase()) {
            case "shift" -> EncryptionAlgorithm.SHIFT;
            case "rsa" -> EncryptionAlgorithm.RSA;
            default -> throw new IllegalStateException("Unexpected value: " + arg);
        };
    }
}
