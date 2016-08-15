package Client;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Adam on 8/11/2016.
 */

public class TerminalController implements Initializable {


    private ClassLoader classLoader = this.getClass().getClassLoader();



    private double xOffset;
    private double yOffset;

    int maxLength = 30;

    ArrayList<Label> history = new ArrayList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGraphics();
        setPreferences();
        createListeners();


    }

    private void setGraphics(){

        ImageView closeIcon = new ImageView();
        closeIcon.setImage(new javafx.scene.image.Image(classLoader.getResourceAsStream("Images/closeIcon.png")));
        closeIcon.setFitWidth(15);
        closeIcon.setFitHeight(15);
        closeIcon.setBlendMode(BlendMode.EXCLUSION);
        closeButton.setGraphic(closeIcon);

        ImageView minimizeIcon = new ImageView();
        minimizeIcon.setImage(new javafx.scene.image.Image(classLoader.getResourceAsStream("Images/minimizeIcon.png")));
        minimizeIcon.setFitWidth(15);
        minimizeIcon.setFitHeight(15);
        minimizeIcon.setBlendMode(BlendMode.EXCLUSION);
        minimizeButton.setGraphic(minimizeIcon);

    }

    private void setPreferences(){

        chatWindowScrollPane.vvalueProperty().bind(chatWindow.heightProperty());

    }

    private void createListeners(){

        titleBar.setOnMousePressed(Event -> {
            xOffset = titleBar.getScene().getWindow().getX() - MouseInfo.getPointerInfo().getLocation().getX();
            yOffset = titleBar.getScene().getWindow().getY() - MouseInfo.getPointerInfo().getLocation().getY();
        });

        titleBar.setOnMouseDragged(Event -> {
            titleBar.getScene().getWindow().setX(MouseInfo.getPointerInfo().getLocation().getX() + xOffset);
            titleBar.getScene().getWindow().setY(MouseInfo.getPointerInfo().getLocation().getY() + yOffset);

        });

        closeButton.setOnAction(event -> closeWindow() );

        chatLine.setOnAction(Event ->{
            for(int i = 0; i < 700; i++){
                append("Line: " + i + " " + chatLine.getText());
            }

            chatLine.setText("");
        });
    }

    private void closeWindow(){

        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();

    }

    private void append(String text){


        Label message = new Label(text);

        chatWindow.getChildren().add(message);
    }










    //--------------------------------------------------JAVAFX INITIALIZATION-------------------------------------------
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane chatWindowScrollPane;

    @FXML
    private TextField chatLine;

    @FXML
    private VBox chatWindow;

    @FXML
    private Button closeButton;

    @FXML
    private Button createRoomButton;

    @FXML
    private Button joinRoomButton;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Button minimizeButton;

    @FXML
    private ListView<?> onlineList;

    @FXML
    private ToolBar titleBar;

    @FXML
    private ImageView titleImageView;


    @FXML
    void initialize() {
        assert chatLine != null : "fx:id=\"chatLine\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindow != null : "fx:id=\"chatWindow\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert createRoomButton != null : "fx:id=\"createRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert joinRoomButton != null : "fx:id=\"joinRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert mainPanel != null : "fx:id=\"mainPanel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert minimizeButton != null : "fx:id=\"minimizeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert onlineList != null : "fx:id=\"onlineList\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleBar != null : "fx:id=\"titleBar\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleImageView != null : "fx:id=\"titleImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";


    }


}
