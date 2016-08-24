package RoomPane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Adam on 8/23/2016.
 */
public class RoomPaneWindow extends Application {


    private ClassLoader classLoader = this.getClass().getClassLoader();
    FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource("RoomPane/RoomPaneFXML.fxml"));


        public RoomPaneWindow(){

        }

        public static void main(String[] args) {
            Application.launch(RoomPane.RoomPaneWindow.class, (java.lang.String[]) null);
        }


        @Override
        public void start(Stage primaryStage) {
            try {
                AnchorPane page = fxmlLoader.load();

                Scene scene = new Scene(page);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(scene);
                primaryStage.show();

            } catch (Exception ex) {
                Logger.getLogger(RoomPane.RoomPaneWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }




}
