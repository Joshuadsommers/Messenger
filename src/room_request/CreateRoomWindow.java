package room_request;

import RoomPane.RoomPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import objects.User;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Adam on 8/18/2016.
 */
public class CreateRoomWindow extends Application {
    private ClassLoader classLoader = this.getClass().getClassLoader();
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource("room_request/RoomRequestFXML.fxml"));
    HashSet<User> onlineList;
    public CreateRoomWindow(HashSet<User> onlineList){
        this.onlineList = onlineList;

    }

    public static void main(String[] args) {
        Application.launch(CreateRoomWindow.class, (java.lang.String[]) null);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            AnchorPane page = fxmlLoader.load();
            Scene scene = new Scene(page);

            CreateRoomController controller = fxmlLoader.<CreateRoomController>getController();
            controller.start(onlineList);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            Logger.getLogger(CreateRoomWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
