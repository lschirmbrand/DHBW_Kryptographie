package configuration;

import gui.GUILogger;

public enum Configuration {
    instance;

    public String userDirectory = System.getProperty("user.dir");
    public String fileSeparator = System.getProperty("file.separator");
    public String commonPathToJavaArchive = userDirectory + fileSeparator + "components" + fileSeparator;
    public String commonPathToKeyFile = userDirectory + fileSeparator + "keyfiles" + fileSeparator;
    public String lineSeparator = System.getProperty("line.separator");


    // ShiftBase
    public String pathToShiftBaseJavaArchive = commonPathToJavaArchive + "shiftBase" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "ShiftBase.jar";

    // ShiftCracker
    public String pathToShiftCrackerJavaArchive = commonPathToJavaArchive + "shiftCracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "ShiftCracker.jar";

    // RSABase
    public String pathToRSABaseJavaArchive = commonPathToJavaArchive + "rsaBase" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "RSABase.jar";

    // RSACracker
    public String pathToRSACrackerJavaArchive = commonPathToJavaArchive + "rsaCracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "RSACracker.jar";


    // Database
    public final String databaseDirectory = userDirectory + fileSeparator + "database" + fileSeparator;
    public final String databaseFile = databaseDirectory + "cryptoDB.db";
    public final String dbDriverName = "jdbc:hsqldb:";
    public final String dbUsername = "sa";
    public final String dbPassword = "";


    public GUILogger guiLogger;
    public boolean debugMode;

    // Log-Directory
    public final String logsDirectory = userDirectory + fileSeparator + "log" + fileSeparator;
}
