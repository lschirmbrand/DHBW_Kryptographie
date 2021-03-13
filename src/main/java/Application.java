import database.HSQLDB;
import database.MSADBService;
import gui.GUI;

public class Application {

    MSADBService dbService = MSADBService.instance;

    public static void main(String[] args) {

        Application app = new Application();
        app.init();
        javafx.application.Application.launch(GUI.class);
    }

    public void init(){
        dbService.setupConnection();
    }

    private void startupGUI(){

    }

    private void close(){
        HSQLDB.instance.shutdown();
    }

    private void initNetworks(){

    }
}
