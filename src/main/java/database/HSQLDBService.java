package database;

import database.models.Channel;
import database.models.Participant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public enum HSQLDBService implements IDBService {
    instance;

    private final HSQLDB db = HSQLDB.instance;

    @Override
    public void setup() {
        db.setupDatabase();
    }

    @Override
    public void dropChannel(String channelName) {
        db.update("DELETE FROM CHANNEL WHERE name='" + channelName + "'");
    }

    @Override
    public void insertType(String type) {
        type = type.toLowerCase();
        StringBuilder sb = new StringBuilder();
        if (getTypeID(type) > 0) return;

        sb.append("INSERT INTO types (name)");
        sb.append(" VALUES");
        sb.append(" (").append("'").append(type).append("'").append(")");
        System.out.println("SQL-Statement Builder: " + sb.toString());

        db.update(sb.toString());
    }

    @Override
    public void insertAlgorithm(String algorithm) {
        StringBuilder sb = new StringBuilder();
        if (getAlgorithmID(algorithm) > 0) return;
        sb.append("INSERT INTO algorithms (").append("name").append(")");
        sb.append(" VALUES ('").append(algorithm).append("')");
        System.out.println("SQL-Statement Builder: " + sb.toString());
        db.update(sb.toString());
    }

    @Override
    public void insertMessage(String participantSender, String participantReceiver, String algorithm, String keyFile, String plainMessage, String encryptedMessage) {
        if (!getAlgorithms().contains(algorithm)) {
            insertAlgorithm(algorithm);
        }

        int participantFromID = getParticipantID(participantSender);
        int participantToID = getParticipantID(participantReceiver);
        int algorithmID = getAlgorithmID(algorithm);
        long timeStamp = Instant.now().getEpochSecond();

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO messages (PARTICIPANT_FROM_ID, PARTICIPANT_TO_ID, PLAIN_MESSAGE, ALGORITHM_ID, ENCRYPTED_MESSAGE, KEYFILE, TIMESTAMP)");
        sqlStringBuilder.append(" VALUES (");
        sqlStringBuilder.append(MessageFormat.format("{0}, {1}, ''{2}'', {3}, ''{4}'', ''{5}'', {6} ",
                participantFromID, participantToID, plainMessage, algorithmID, encryptedMessage, keyFile, Long.toString(timeStamp)));
        sqlStringBuilder.append(")");
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.update(sqlStringBuilder.toString());
    }

    @Override
    public void insertParticipant(String name, String type) {

        // insert type if not exists
        if (!getTypes().contains(type)) {
            insertType(type);
        }

        name = name.toLowerCase();
        int typeID = getTypeID(type);

        int id = getParticipants().size() + 1;

        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (id, name, type_id)")
                .append(MessageFormat.format(" VALUES ({0}, ''{1}'', {2})", id, name, typeID));
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.update(sqlStringBuilder.toString());

        db.createTablePostbox(name);
    }

    @Override
    public void insertChannel(String name, String participantA, String participantB) {
        name = name.toLowerCase();
        int participantA_ID = getParticipantID(participantA);
        int participantB_ID = getParticipantID(participantB);
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (name, participant_01, participant_02)");
        sqlStringBuilder.append(MessageFormat.format(" VALUES (''{0}'',{1},{2})", name, participantA_ID, participantB_ID));
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.update(sqlStringBuilder.toString());
    }

    @Override
    public void insertPostboxMessage(String participantSender, String participantReceiver, String message) {
        if (!participantExists(participantSender) || !participantExists(participantReceiver)) {
            System.out.println("Could not save postbox message, participant not found.");
        }
        int participantFromID = getParticipantID(participantSender);
        long timeStamp = Instant.now().getEpochSecond();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox_").append(participantReceiver).append(" (participant_from_id, message, timestamp)");
        sqlStringBuilder.append(MessageFormat.format(" VALUES ({0}, ''{1}'', {2})",
                participantFromID, message, Long.toString(timeStamp)));
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.update(sqlStringBuilder.toString());
    }

    @Override
    public List<String> getAlgorithms() {
        List<String> algorithms = new ArrayList<>();
        try {
            ResultSet resultSet = db.select("SELECT * from ALGORITHMS");
            while (resultSet.next()) {
                algorithms.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return algorithms;
    }

    @Override
    public List<String> getTypes() {
        List<String> types = new ArrayList<>();
        try {
            ResultSet resultSet = db.select("SELECT * from TYPES");
            while (resultSet.next()) {
                types.add(resultSet.getString("name"));
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return types;
    }

    @Override
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        try {
            ResultSet resultSet = db.select("SELECT * from PARTICIPANTS");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = getTypeName(resultSet.getInt("type_id"));
                Participant p = new Participant(name, type);
                participants.add(p);
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return participants;
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> channelList = new ArrayList<>();

        try {
            ResultSet resultSet = db.select("SELECT * from channel");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int participant1ID = resultSet.getInt("participant_01");
                int participant2ID = resultSet.getInt("participant_02");
                Participant participantA = getParticipant(participant1ID);
                Participant participantB = getParticipant(participant2ID);
                channelList.add(new Channel(name, participantA, participantB));
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return channelList;
    }

    @Override
    public String getOneParticipantType(String participantName) {
        if (participantName == null) return null;
        participantName = participantName.toLowerCase();
        int typeID;
        try {
            ResultSet resultSet = db.select("SELECT TYPE_ID from PARTICIPANTS where name='" + participantName + "'");
            if (!resultSet.next()) {
                throw new SQLException(participantName + " participant wasn't found.");
            }
            typeID = resultSet.getInt("TYPE_ID");
            return getTypeName(typeID);

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public Participant getOneParticipant(String participantName) {
        participantName = participantName.toLowerCase();
        if (participantExists(participantName)) {
            return new Participant(participantName, getOneParticipantType(participantName));
        }
        return null;
    }

    @Override
    public boolean participantExists(String name) {
        int participantID = getParticipantID(name.toLowerCase());
        return participantID != -1;
    }


    private int getTypeID(String name) {
        name = name.toLowerCase();
        String sqlStatement = "SELECT ID from TYPES where lower(name)=lower('" + name + "')";

        try {
            ResultSet resultSet = db.select(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(name + " wasn't found in Type-Table");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return -1;
    }

    private int getParticipantID(String name) {
        name = name.toLowerCase();
        String sqlStatement = "SELECT ID from PARTICIPANTS where name='" + name + "'";
        try {
            ResultSet resultSet = db.select(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(name + " wasn't found in Participant-Table.");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return -1;
    }

    private String getParticipantName(int participantID) {
        String sqlStatement = "SELECT name from participants where ID=" + participantID;
        try {
            ResultSet resultSet = db.select(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(" Name of participant wasn't found for ID:" + participantID);
            }
            return resultSet.getString("name");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    private String getTypeName(int typeID) {
        String sqlStatement = "SELECT name from TYPES where ID=" + typeID;
        try {
            ResultSet resultSet = db.select(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException("No name of type found for ID: " + typeID);
            }
            return resultSet.getString("name");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    private int getAlgorithmID(String algorithm) {
        String sqlStatement = "SELECT ID from ALGORITHMS where LOWER(name)=LOWER('" + algorithm + "')";
        try {
            ResultSet resultSet = db.select(sqlStatement);
            if (!resultSet.next()) {
                throw new SQLException(algorithm + " algorithm wasn't found in Algorithm Table");
            }
            return resultSet.getInt("ID");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return -1;
    }

    private Participant getParticipant(int partID) {
        String name = getParticipantName(partID);
        return new Participant(name, getOneParticipantType(name));
    }
}
