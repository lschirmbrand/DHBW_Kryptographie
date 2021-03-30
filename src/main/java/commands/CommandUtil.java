package commands;

public class CommandUtil {

    private CommandUtil() {

    }

    public static ICommand getShowAlgorithmCommand() {
        return new ShowAlgorithmCommand();
    }

    public static ICommand getEncryptMessageCommand() {
        return new EncryptMessageCommand();
    }

    public static ICommand getDecryptMessageCommand() {
        return new DecryptMessageCommand();
    }

    public static ICommand getCrackMessageCommand() {
        return new CrackMessageCommand();
    }

    public static ICommand getCreateChannelCommand() {
        return new CreateChannelCommand();
    }

    public static ICommand getDropChannelCommand() {
        return new DropChannelCommand();
    }

    public static ICommand getShowChannelCommand() {
        return new ShowChannelCommand();
    }

    public static ICommand getIntruderChannelCommand() {
        return new IntruderChannelCommand();
    }

    public static ICommand getRegisterParticipantCommand() {
        return new RegisterParticipantCommand();
    }

    public static ICommand getSendMessageCommand() {
        return new SendMessageCommand();
    }


}
