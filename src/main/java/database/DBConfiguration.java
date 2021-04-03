package database;

public enum DBConfiguration {
    instance;

    final String driverName = "jdbc:hsqldb:";
    final String username = "sa";
    final String password = "";
    final String userDirectory = System.getProperty("user.dir");
    final String fileSeparator = System.getProperty("file.separator");
    final String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    final String databaseFile = dataDirectory + "datastore.db";
}