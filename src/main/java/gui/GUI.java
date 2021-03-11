package gui;

import configuration.Configuration;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GUI {

    TextArea outputArea;
    TextArea inputArea;

    private Controller guiController;

    public void startUp(Stage mainStage){

        guiController = new Controller(this);

        mainStage.setTitle("Name to be done");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 15, 20, 15));
        hBox.setSpacing(15);
        hBox.setStyle("-fx-background-color: #62e77d");

        Button btnExecute = new Button("Execute");
        btnExecute.setPrefSize(100, 24);

        Button btnClose = new Button("Close");
        btnClose.setPrefSize(100, 24);

        btnExecute.setOnAction(event -> guiController.executeCommand(inputArea.getText()));

        btnClose.setOnAction(event -> guiController.closeGUI());

        inputArea = new TextArea();
        inputArea.setWrapText(true);

        outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        hBox.getChildren().addAll(hBox, inputArea, outputArea);

        VBox vBox = new VBox(15);
        vBox.setPadding(new Insets(20,20,20,20));
        vBox.getChildren().addAll(hBox, inputArea, outputArea);

        Scene guiScene = new Scene(vBox, 1000, 500);
        mainStage.setScene(guiScene);
        mainStage.show();

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
