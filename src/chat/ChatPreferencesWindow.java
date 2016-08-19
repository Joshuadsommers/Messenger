package chat;

import Client.TerminalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Adam on 8/18/2016.
 */
public class ChatPreferencesWindow extends Application {
    private ClassLoader classLoader = this.getClass().getClassLoader();
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource("chat/ChatPreferencesFXML.fxml"));
    private TerminalController controller;
    private ChatPreferencesController chatController;

    public ChatPreferencesWindow(TerminalController controller){
        this.controller = controller;
    }

    public static void main(String[] args) {
        Application.launch(ChatPreferencesWindow.class, (java.lang.String[]) null);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane page = fxmlLoader.load();
            chatController = fxmlLoader.<ChatPreferencesController>getController();
            chatController.start(this.controller);
            Scene scene = new Scene(page);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            Logger.getLogger(ChatPreferencesWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
