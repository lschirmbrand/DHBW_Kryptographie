package gui;


import configuration.Configuration;
import core.SecurityAgency;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Controller {

    private final GUI gui;
    private final SecurityAgency securityAgency;

    public Controller(GUI gui) {
        this.gui = gui;
        securityAgency = new SecurityAgency();

        Configuration.instance.guiLogger = new GUILogger(this::displayText);
    }

    public void closeGUI() {
        System.exit(0);
    }

    public void displayText(String text) {
        gui.setOutputText(text);
    }

    public void showLogFile() {
        try {
            Path path = Files.walk(Paths.get(Configuration.instance.logsDirectory))
                    .filter(Files::isRegularFile)
                    .max((p1, p2) -> {
                        long time1 = Long.parseLong(p1.getFileName().toString().split("(encrypt|decrypt)_(rsa|shift)_|.txt")[1]);
                        long time2 = Long.parseLong(p2.getFileName().toString().split("(encrypt|decrypt)_(rsa|shift)_|.txt")[1]);

                        return (int) (time1 - time2);
                    })
                    .orElseThrow(() -> new RuntimeException("couldn't find a file"));
            BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));

            displayText("-------------------------------");
            displayText("Log-File: " + path.getFileName().toString() + "\n");

            String line;

            while ((line = reader.readLine()) != null) {
                displayText(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disableDebug() {
        displayText("-------------------------------");
        displayText("Debug-Mode turned: On");
        Configuration.instance.debugMode = true;
    }

    public void enableDebug() {
        displayText("-------------------------------");
        displayText("Debug-Mode turned: Off");
        Configuration.instance.debugMode = false;
    }

    public void executeCommand(String command) {
        // one command per line
        Stream.of(command.split(Configuration.instance.lineSeparator))
                // filter blank lines
                .filter(s -> !s.isBlank())
                // execute line by line
                .forEach(securityAgency.getInterpreter()::interpret);

    }
}
