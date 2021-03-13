package database.models;

public class PostboxMessage {
    private final Participant participantSender;
    private final Participant participantReceiver;
    private final String message;
    private final String timestamp;

    public PostboxMessage(Participant participantSender, Participant participantReceiver, String message, String timestamp){
        this.participantSender = participantSender;
        this.participantReceiver = participantReceiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Participant getParticipantSender() {
        return participantSender;
    }

    public Participant getParticipantReceiver() {
        return participantReceiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
