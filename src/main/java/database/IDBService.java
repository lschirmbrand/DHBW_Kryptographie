package database;

import database.models.Channel;
import database.models.Message;
import database.models.Participant;
import database.models.PostboxMessage;

import java.util.List;

public interface IDBService {

    void setup();

    void dropChannel(String channelName);

    // Inserts

    void insertType(String type);

    void insertAlgorithm(String algorithm);

    void insertMessage(String participantSender, String participantReceiver, String algorithm, String keyFile, String plainMessage, String encryptedMessage);

    void insertParticipant(String name, String type);

    void insertChannel(String name, String participantA, String participantB);

    void insertPostboxMessage(String participantSender, String participantReceiver, String message);


    // Getter

    List<String> getAlgorithms();

    List<String> getTypes();

    List<Participant> getParticipants();

    List<Channel> getChannels();

    String getOneParticipantType(String participantName);

    Participant getOneParticipant(String participantName);


    // Check for existence

    boolean participantExists(String name);
}
