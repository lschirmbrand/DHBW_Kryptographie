package database;

import database.models.Channel;
import database.models.Message;
import database.models.Participant;
import database.models.PostboxMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public enum MSADBService implements IMSADBService {
    instance;

    private HSQLDB db = HSQLDB.instance;
    private Connection conn;

    @Override
    public void setupConnection() {
        db.setupConnection();
    }

    @Override
    public void createAllTables() {
        db.createTableAlgorithms();
        db.createTableParticipants();
        db.createTableChannel();
        db.createTableMessages();
        db.createTableTypes();
    }

    @Override
    public void dropAllTables() {

    }

    @Override
    public void shutdown() {
        db.shutdown();
    }

    @Override
    public void dropChannel() {

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

        db.outerUpdate(sb.toString());
    }

    @Override
    public void insertAlgorithm(String algorithm) {
        StringBuilder sb = new StringBuilder();
        if (getAlgorithmID(algorithm) > 0) return;
        sb.append("INSERT INTO algorithms (").append("name").append(")");
        sb.append(" VALUES ");
        sb.append("(").append("'").append(algorithm).append("'");
        sb.append(")");
        System.out.println("SQL-Statement Builder: " + sb.toString());
        db.outerUpdate(sb.toString());
    }

    @Override
    public void insertMessage(String participantSender, String participantReceiver, String algorithm, String keyFile, String plainMessage, String encryptedMessage) {
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
        db.outerUpdate(sqlStringBuilder.toString());
    }

    @Override
    public void insertMessage(Message message) {
        insertMessage(message.getParticipantSender().getName(), message.getParticipantReceiver().getName(),
                message.getPlainMessage(), message.getAlgorithm(), message.getEncryptedMessage(),
                message.getKeyfile());
    }

    @Override
    public void insertParticipant(String name, String type) {
        name = name.toLowerCase();
        int typeID = getTypeID(type);
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO participants (").append("name").append(",").append("type_id").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("',");
        sqlStringBuilder.append(typeID);
        sqlStringBuilder.append(")");
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.outerUpdate(sqlStringBuilder.toString());

        db.createTablePostbox(name);
    }

    @Override
    public void insertParticipant(Participant participant) {
        insertParticipant(participant.getName(), participant.getType());
    }

    @Override
    public void insertChannel(String name, String participantA, String participantB) {
        name = name.toLowerCase();
        int participantA_ID = getParticipantID(participantA);
        int participantB_ID = getParticipantID(participantB);
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO channel (").append("name").append(",").append("participant_01").append(",").append("participant_02").append(")");
        sqlStringBuilder.append(" VALUES ");
        sqlStringBuilder.append("(").append("'").append(name).append("',");
        sqlStringBuilder.append(participantA_ID).append(",");
        sqlStringBuilder.append(participantB_ID);
        sqlStringBuilder.append(")");
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.outerUpdate(sqlStringBuilder.toString());
    }

    @Override
    public void insertChannel(Channel channel) {
        insertChannel(channel.getName(), channel.getParticipantA().getName(),
                channel.getParticipantB().getName());
    }

    @Override
    public void insertPostboxMessage(String participantSender, String participantReceiver, String message) {
        if (!participantExists(participantSender) || !participantExists(participantReceiver)) {
            System.out.println("Could not save postbox message, participant not found.");
        }
        int participantFromID = getParticipantID(participantSender);
        long timeStamp = Instant.now().getEpochSecond();
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append("INSERT INTO postbox_" + participantReceiver + " (participant_from_id, message, timestamp)");
        sqlStringBuilder.append(" VALUES (");
        sqlStringBuilder.append(MessageFormat.format("{0}, ''{1}'', {2} ",
                participantFromID, message, Long.toString(timeStamp)));
        sqlStringBuilder.append(")");
        System.out.println("SQL-Statement Builder: " + sqlStringBuilder.toString());
        db.outerUpdate(sqlStringBuilder.toString());
    }

    @Override
    public void insertPostboxMessage(PostboxMessage postboxMessage) {
        insertPostboxMessage(postboxMessage.getParticipantReceiver().getName(),
                postboxMessage.getParticipantSender().getName(), postboxMessage.getMessage());
    }


    @Override
    public List<String> getAlgorithms() {
        List<String> algorithms = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT * from ALGORITHMS";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    algorithms.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return algorithms;
    }

    @Override
    public List<String> getTypes() {
        List<String> types = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT * from TYPES";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    types.add(resultSet.getString("name"));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return types;
    }

    @Override
    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT * from PARTICIPANTS";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String type = getTypeName(resultSet.getInt("type_id"));
                    Participant p = new Participant(name, type);
                    participants.add(p);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return participants;
    }

    @Override
    public List<Channel> getChannels() {
        List<Channel> channelList = new ArrayList<>();

        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT * from channel";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    int participant1ID = resultSet.getInt("participant_01");
                    int participant2ID = resultSet.getInt("participant_02");
                    Participant participantA = getParticipant(participant1ID);
                    Participant participantB = getParticipant(participant2ID);
                    channelList.add(getOneChannel(participantA.getName(), participantB.getName()));
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return channelList;
    }

    @Override
    public List<PostboxMessage> getPostboxMessages(String participant) {
        List<PostboxMessage> msgList = new ArrayList<>();
        if (!participantExists(participant)) {
            System.out.println("Couldn't get postbox message, participant wasn't found.");
        }
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT * from POSTBOX_" + participant;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                while (resultSet.next()) {
                    int partFromID = resultSet.getInt("participant_from_id");
                    String partFromName = getParticipantName(partFromID);
                    Participant partFrom = new Participant(partFromName, getOneParticipantType(partFromName));
                    Participant partTo = new Participant(participant, getOneParticipantType(participant));
                    String timestamp = Integer.toString(resultSet.getInt("timestamp"));
                    String message = resultSet.getString("message");
                    PostboxMessage pbM = new PostboxMessage(partFrom, partTo, message, timestamp);
                    msgList.add(pbM);
                }
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return msgList;
    }

    @Override
    public Channel getOneChannel(String participantA, String participantB) {
        try (Statement statement = conn.createStatement()) {
            int partAID = getParticipantID(participantA);
            int partBID = getParticipantID(participantB);
            String sqlStatement = MessageFormat.format(
                    "SELECT name from channel where (participant_01=''{0}'' AND participant_02=''{1}'') or (participant_01=''{1}'' AND participant_02=''{0}'')",
                    partAID, partBID);
            String channelName;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException("No channel found with participants: " + participantA + " & " + participantB);
                }
                channelName = resultSet.getString("name");
            }
            return new Channel(channelName, getOneParticipant(participantA), getOneParticipant(participantB));
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public String getOneParticipantType(String participantName) {
        if (participantName == null) return null;
        participantName = participantName.toLowerCase();
        int typeID = -1;
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT TYPE_ID from PARTICIPANTS where name='" + participantName + "'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(participantName + " participant wasn't found.");
                }
                typeID = resultSet.getInt("TYPE_ID");
            }
            String typeName = getTypeName(typeID);
            return typeName;

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
    public boolean channelExists(String channelName) {
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT name from channel where LOWER(name)='" + channelName.toLowerCase() + "'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(channelName + " channel wasn't found");
                }
            }
            return true;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }

    @Override
    public boolean participantExists(String name) {
        int participantID = getParticipantID(name.toLowerCase());
        return participantID != -1;
    }


    private int getTypeID(String name) {
        try (Statement statement = conn.createStatement()) {
            name = name.toLowerCase();
            String sqlStatement = "SELECT ID from TYPES where lower(name)=lower('" + name + "')";

            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(name + " wasn't found in Type-Table");
                }

                return resultSet.getInt("ID");
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return -1;
    }

    private int getParticipantID(String name) {
        try (Statement statement = conn.createStatement()) {
            name = name.toLowerCase();
            String sqlStatement = "SELECT ID from PARTICIPANTS where name='" + name + "'";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(name + " wasn't found in Participant-Table.");
                }
                return resultSet.getInt("ID");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return -1;
    }

    private String getParticipantName(int participantID) {
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT name from participants where ID=" + participantID;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(" Name of participant wasn't found for ID:" + participantID);
                }
                return resultSet.getString("name");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    private String getTypeName(int typeID) {
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT name from TYPES where ID=" + typeID;
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException("No name of type found for ID: " + typeID);
                }
                return resultSet.getString("name");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    private int getAlgorithmID(String algorithm) {
        try (Statement statement = conn.createStatement()) {
            String sqlStatement = "SELECT ID from ALGORITHMS where LOWER(name)=LOWER('" + algorithm + "')";
            try (ResultSet resultSet = statement.executeQuery(sqlStatement)) {
                if (!resultSet.next()) {
                    throw new SQLException(algorithm + " algorithm wasn't found in Algorithm Table");
                }
                return resultSet.getInt("ID");
            }
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
