import database.HSQLDB;
import database.HSQLDBService;
import gui.GUI;

public class Application {

    HSQLDBService dbService = HSQLDBService.instance;

    public static void main(String[] args) {

        Application app = new Application();
        app.init();
        javafx.application.Application.launch(GUI.class);
    }

    public void init() {
        dbService.setupConnection();
    }

    private void startupGUI() {

    }

    private void close() {
        HSQLDB.instance.shutdown();
    }

    private void initNetworks() {

    }
}
