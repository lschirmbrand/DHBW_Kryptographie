package database.models;

public class Channel {

    private final String name;
    private final Participant participantA;
    private final Participant participantB;

    public Channel(String name, Participant participantA, Participant participantB){
        this.name = name;
        this.participantA = participantA;
        this.participantB = participantB;
    }

    public String getName(){
        return this.name;
    }

    public Participant getParticipantA(){
        return this.participantA;
    }

    public Participant getParticipantB(){
        return this.participantB;
    }

}
