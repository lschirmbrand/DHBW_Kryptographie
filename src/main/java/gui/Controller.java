package gui;


import configuration.Configuration;
import core.SecurityAgency;

public class Controller {

    private final GUI gui;
    private final Configuration configuration = Configuration.instance;

    private final SecurityAgency securityAgency;

    public Controller(GUI gui) {
        this.gui = gui;
        securityAgency = new SecurityAgency();
    }

    public void closeGUI() {
        System.exit(0);
    }

    public void displayText(String text) {
        gui.setOutputText(text);
    }

    public void showLog() {

    }

    public void disableLogging() {
        displayText("Logging turned: On");
        configuration.enableLogging();
    }

    public void enableLogging() {
        displayText("Logging turned: Off");
        configuration.disableLogging();
    }

    public void executeCommand(String command) {
        String res = securityAgency.getInterpreter().interpret(command);
        gui.setOutputText(res);
    }

    public boolean isLoggingEnabled() {
        return true;
    }
}
