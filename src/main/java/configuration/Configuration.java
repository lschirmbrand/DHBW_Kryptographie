package configuration;

import gui.GUILogger;

public enum Configuration {
    instance;

    public GUILogger guiLogger;
    public boolean debugMode = false;

    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String commonPathToJavaArchive = userDirectory + fileSeparator + "components" + fileSeparator;
    public final String commonPathToKeyFile = userDirectory + fileSeparator + "keyfiles" + fileSeparator;
    public final String lineSeparator = System.getProperty("line.separator");

    // Log-Directory
    public final String logsDirectory = userDirectory + fileSeparator + "log" + fileSeparator;

    // ShiftBase
    public final String pathToShiftBaseJavaArchive = commonPathToJavaArchive + "shiftBase" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "ShiftBase.jar";

    // ShiftCracker
    public final String pathToShiftCrackerJavaArchive = commonPathToJavaArchive + "shiftCracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "ShiftCracker.jar";

    // RSABase
    public final String pathToRSABaseJavaArchive = commonPathToJavaArchive + "rsaBase" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "RSABase.jar";

    // RSACracker
    public final String pathToRSACrackerJavaArchive = commonPathToJavaArchive + "rsaCracker" + fileSeparator + "build"
            + fileSeparator + "libs" + fileSeparator + "RSACracker.jar";
}
