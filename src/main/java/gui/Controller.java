package gui;


import configuration.Configuration;

public class Controller {

    private GUI gui;
    private Configuration configuration = Configuration.instance;

    public Controller(GUI gui) {
        this.gui = gui;
    }

    public void closeGUI(){
        System.exit(0);
    }

    public void displayText(String text){
        gui.setOutputText(text);
    }

    public void showLog(){

    }

    public void disableLogging(){
        displayText("Logging turned: On");
        configuration.enableLogging();
    }

    public void enableLogging(){
        displayText("Logging turned: Off");
        configuration.disableLogging();
    }

    public void executeCommand(String command){

    }

    public boolean isLoggingEnabled(){
        return true;
    }
}
