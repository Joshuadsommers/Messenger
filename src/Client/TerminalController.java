package Client;

import Server.Room;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import objects.MasterClassUser;
import objects.Message;
import objects.MessageLabel;

import java.awt.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adam on 8/11/2016.
 */

public class TerminalController implements Initializable {


    private ClassLoader classLoader = this.getClass().getClassLoader();
    private ConnectionHandler connectionHandler;

    public ArrayList<Room> activeRooms = new ArrayList<>();

    private Thread thread;
    private String fontType = "Verdana";
    private Color fontColor = Color.BLUE;

    private int fontStyle = Font.PLAIN;


    private int fontSize = 14;

    private double xOffset;
    private double yOffset;


    ArrayList<Label> history = new ArrayList<>();
    Map<String, Color> colorMap = new HashMap<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadColors();
        setGraphics();
        setPreferences();
        createListeners();
        connect();


    }

    private void connect(){



        Thread thread = new Thread(() ->{
            connectionHandler.connect();
        });
        thread.start();

    }
    private void loadColors(){
        Class clazz = null;
        try {
            clazz = Class.forName("javafx.scene.paint.Color");

            if (clazz != null) {
                Field[] field = clazz.getFields();
                for (int i = 0; i < field.length; i++) {
                    Field f = field[i];
                    Object obj = f.get(null);
                    if(obj instanceof Color){
                        colorMap.put(f.getName(), (Color) obj);
                    }

                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void setGraphics(){

        ImageView closeIcon = new ImageView();
        closeIcon.setImage(new Image(classLoader.getResourceAsStream("Images/closeIcon.png")));
        closeIcon.setFitWidth(15);
        closeIcon.setFitHeight(15);
        closeIcon.setBlendMode(BlendMode.EXCLUSION);
        closeButton.setGraphic(closeIcon);

        ImageView minimizeIcon = new ImageView();
        minimizeIcon.setImage(new Image(classLoader.getResourceAsStream("Images/minimizeIcon.png")));
        minimizeIcon.setFitWidth(15);
        minimizeIcon.setFitHeight(15);
        minimizeIcon.setBlendMode(BlendMode.EXCLUSION);
        minimizeButton.setGraphic(minimizeIcon);

        attachmentImageView.setImage(new Image(classLoader.getResourceAsStream("Images/attachmentIcon.png")));

    }

    private void setPreferences(){

        chatWindowScrollPane.vvalueProperty().bind(chatWindow.heightProperty());
        fontSizeComboBox.getItems().clear();
        fontSizeComboBox.getItems().add(14);
        fontSizeComboBox.getItems().add(16);
        fontSizeComboBox.getItems().add(18);

        fontColorComboBox.getItems().clear();
        fontColorComboBox.getItems().addAll(colorMap.keySet());

        fontTypeComboBox.getItems().clear();
        fontTypeComboBox.getItems().addAll(javafx.scene.text.Font.getFamilies());

        connectionHandler = new ConnectionHandler(MasterClassUser.user, this);

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

            String text = chatLine.getText();

            if(text.length() > 0){
                Message message = new Message(0, MasterClassUser.user, text, false);
                sendMessage(message);

                chatLine.setText("");
            }
        });

        attachmentImageView.setOnMouseEntered(Event -> {
            attachmentImageView.setScaleX(1.2);
            attachmentImageView.setScaleY(1.2);

        });

        attachmentImageView.setOnMouseExited(Event -> {
            attachmentImageView.setScaleX(1);
            attachmentImageView.setScaleY(1);

        });

        fontSizeComboBox.setOnAction(Event ->{
            fontSize = fontSizeComboBox.getSelectionModel().getSelectedItem();
        });

        fontColorComboBox.setOnAction(Event ->{
            fontColor = colorMap.get(fontColorComboBox.getSelectionModel().getSelectedItem());
        });

        fontTypeComboBox.setOnAction(Event ->{
            fontType = fontTypeComboBox.getSelectionModel().getSelectedItem();
        });

        boldButton.setOnAction(Event ->{
            if(!boldButton.isSelected()){
                fontStyle = Font.BOLD;
                italicButton.setSelected(false);
            }
            else fontStyle = Font.PLAIN;

        });

        italicButton.setOnAction(Event ->{
            if(!italicButton.isSelected()){
                fontStyle = Font.ITALIC;
                boldButton.setSelected(false);
            }
            else fontStyle = Font.PLAIN;
        });


    }

    private void sendMessage(Message message){

        connectionHandler.sendMessage(message);

    }

    public void receiveInput(Object input){
        if(input.getClass().equals(Message.class)){
            appendMessage((Message) input);
        }
    }

    public void appendMessage(Message message){

        thread = new Thread(() ->{
            MessageLabel messageLabel = new MessageLabel(message);

            messageLabel.setBlendMode(BlendMode.EXCLUSION);
            messageLabel.setFont(new javafx.scene.text.Font(fontType, fontSize));

            messageLabel.setTextFill(fontColor);
            messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
            messageLabel.setWrapText(true);

            chatWindow.getChildren().add(messageLabel);
        });

        Platform.runLater(thread);



    }




    private void closeWindow(){

        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
        System.exit(0);


    }

    private void append(Message message){

        // Label message = new Label();


    }











    //--------------------------------------------------JAVAFX INITIALIZATION-------------------------------------------
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane chatTabPane;

    @FXML
    private AnchorPane centerPane;

    @FXML
    private ImageView attachmentImageView;

    @FXML
    private ToggleButton boldButton;

    @FXML
    private TextField chatLine;

    @FXML
    private VBox chatWindow;

    @FXML
    private ScrollPane chatWindowScrollPane;

    @FXML
    private Button closeButton;

    @FXML
    private Button createRoomButton;

    @FXML
    private ComboBox<String> fontColorComboBox;

    @FXML
    private ComboBox<Integer> fontSizeComboBox;

    @FXML
    private ComboBox<String> fontTypeComboBox;

    @FXML
    private ToggleButton italicButton;

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
        assert attachmentImageView != null : "fx:id=\"attachmentImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert boldButton != null : "fx:id=\"boldButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatLine != null : "fx:id=\"chatLine\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindow != null : "fx:id=\"chatWindow\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindowScrollPane != null : "fx:id=\"chatWindowScrollPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert createRoomButton != null : "fx:id=\"createRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert fontColorComboBox != null : "fx:id=\"fontColorComboBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert fontSizeComboBox != null : "fx:id=\"fontSizeComboBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert fontTypeComboBox != null : "fx:id=\"fontTypeComboBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert italicButton != null : "fx:id=\"italicButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert joinRoomButton != null : "fx:id=\"joinRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert mainPanel != null : "fx:id=\"mainPanel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert minimizeButton != null : "fx:id=\"minimizeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert onlineList != null : "fx:id=\"onlineList\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleBar != null : "fx:id=\"titleBar\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleImageView != null : "fx:id=\"titleImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";


    }
}
