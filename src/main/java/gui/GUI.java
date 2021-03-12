package gui;

import configuration.Configuration;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GUI extends Application {

    TextArea outputArea;
    TextArea inputArea;

    private Controller guiController;

    public void start(Stage primaryStage) {

        guiController = new Controller(this);

        primaryStage.setTitle("MSA | Mergentheim/Mosbach Security Agency");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        Button executeButton = new Button("Execute");
        executeButton.setPrefSize(100, 20);

        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);

        TextArea commandLineArea = new TextArea();
        commandLineArea.setWrapText(true);

        TextArea outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        executeButton.setOnAction(event -> guiController.executeCommand(inputArea.getText()));

        closeButton.setOnAction(event -> guiController.closeGUI());

        inputArea = new TextArea();
        inputArea.setWrapText(true);

        outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vBox = new VBox(15);
        vBox.setPadding(new Insets(20,20,20,20));
        vBox.getChildren().addAll(hBox, inputArea, outputArea);

        Scene guiScene = new Scene(vBox, 1000, 500);
        primaryStage.setScene(guiScene);
        primaryStage.show();

        guiScene.addEventHandler(KeyEvent.KEY_PRESSED, keyInput -> keyPressed(keyInput.getCode()));
    }

    private void keyPressed(KeyCode keyCode){
        switch(keyCode){
            case F3:
                if(guiController.isLoggingEnabled()){
                    guiController.disableLogging();
                } else {
                    guiController.enableLogging();
                }
                break;
            case F5:
                guiController.executeCommand(inputArea.getText());
                break;
            case F8:
                break;

            default:
        }
    }

    public void setOutputText(String text){
        if(outputArea.getText().isBlank()){
            outputArea.appendText(Configuration.instance.lineSeparator + text);
        } else {
            outputArea.appendText(text);
        }
        outputArea.positionCaret(outputArea.getLength());
    }

    public String getOutputText(){
        return outputArea.getText();
    }

    public void clearText(){
        outputArea.setText("");
    }
}
