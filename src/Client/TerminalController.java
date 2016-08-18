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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    private boolean expanded = false;
    private Thread translationThread;

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

        ImageView chatIcon = new ImageView();
        chatIcon.setImage(new Image(classLoader.getResourceAsStream("Images/chat_icon.png")));
        chatIcon.setFitWidth(15);
        chatIcon.setFitHeight(15);
        chatIcon.setBlendMode(BlendMode.EXCLUSION);
        joinRoomButton.setGraphic(chatIcon);

        ImageView menuIcon = new ImageView();
        menuIcon.setImage(new Image(classLoader.getResourceAsStream("Images/menu_icon.png")));
        menuIcon.setFitWidth(15);
        menuIcon.setFitHeight(15);
        menuButton.setGraphic(menuIcon);

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

        /*
        BackgroundImage myBI= new BackgroundImage(new Image("my url",32,32,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
                */

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

        menuButton.setOnAction(Event ->{
            if(expanded){
                collapse();
            }
            else expand();
        });

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
        appendMessage(message); // Appends to own client before sending to server, for faster chatting.
        connectionHandler.sendMessage(message);

    }

    public void receiveInput(Object input){
        if(input.getClass().equals(Message.class)){
            appendMessage((Message) input);
        }
    }

    private synchronized void expand(){


        menuButton.setDisable(true);
        centerPanel.toBack();

        centerPanel.setDisable(true);
        expanded = true;
        translationThread = new Thread(() ->{

            while(menuPanel.getLayoutX() < 0){
                try {
                    menuPanel.setLayoutX(menuPanel.getLayoutX() + 1);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            menuButton.setDisable(false);

        });
        translationThread.start();

/*
        menuPanel.setVisible(true);
        centerPanel.setDisable(true);
        expanded = true;
*/
    }

    private synchronized void collapse(){

        menuPanel.toBack();

        expanded = false;

        menuButton.setDisable(true);

        translationThread = new Thread(() ->{
            while(menuPanel.getLayoutX() > -(menuPanel.getWidth())){

                try {
                    menuPanel.setLayoutX(menuPanel.getLayoutX() - 1);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            menuButton.setDisable(false);
            centerPanel.setDisable(false);

        });
        translationThread.start();
        centerPanel.toFront();
/*


        menuPanel.setVisible(false);
        centerPanel.setDisable(false);
        expanded = false;
*/
    }


    // Appends the message to the area for chat in the client.
    // Method is called from receive Input when a message is received from the server
    // or when we append to the client before sending to the server
    public void appendMessage(Message message){

        thread = new Thread(() ->{
            MessageLabel messageLabel = new MessageLabel(message);

            messageLabel.setBlendMode(BlendMode.EXCLUSION);
            messageLabel.setFont(new javafx.scene.text.Font(fontType, fontSize));
            messageLabel.setTextFill(fontColor);
            messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
            //messageLabel.setStyle("-fx-font-weight: bold;"); // MAKES FONT BOLD
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
    private Button joinRoomToolbarButton;

    @FXML
    private URL location;

    @FXML
    private AnchorPane bannerPane;

    @FXML
    private AnchorPane menuBodyPane;

    @FXML
    private AnchorPane centerPanel;

    @FXML
    private SplitPane menuPanel;

    @FXML
    private Label archiveLabel;

    @FXML
    private ImageView attachmentImageView;

    @FXML
    private ToggleButton boldButton;

    @FXML
    private TextField chatLine;

    @FXML
    private TabPane chatTabPane;

    @FXML
    private VBox chatWindow;

    @FXML
    private ScrollPane chatWindowScrollPane;

    @FXML
    private Button closeButton;

    @FXML
    private Label composeLabel;

    @FXML
    private Button createRoomButton;

    @FXML
    private Label databaseTable;

    @FXML
    private ComboBox<String> fontColorComboBox;

    @FXML
    private ComboBox<Integer> fontSizeComboBox;

    @FXML
    private ComboBox<String> fontTypeComboBox;

    @FXML
    private Label gatewayLabel;

    @FXML
    private Label gravemindLabel;

    @FXML
    private Label inboxLabel;

    @FXML
    private ToggleButton italicButton;

    @FXML
    private Button joinRoomButton;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Button menuButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Label notesLabel;

    @FXML
    private ListView<?> onlineList;

    @FXML
    private Label outboxLabel;

    @FXML
    private Circle pictureCircle;

    @FXML
    private Label rosterLabel;

    @FXML
    private ImageView settingsLabel;

    @FXML
    private Label tasksLabel;

    @FXML
    private ToolBar titleBar;

    @FXML
    private ImageView titleImageView;

    @FXML
    private Label userAliasContactLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label userPermissionsLabel;

    @FXML
    private Label userTitleLabel;


    @FXML
    void initialize() {
        assert archiveLabel != null : "fx:id=\"archiveLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert attachmentImageView != null : "fx:id=\"attachmentImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert boldButton != null : "fx:id=\"boldButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatLine != null : "fx:id=\"chatLine\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatTabPane != null : "fx:id=\"chatTabPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindow != null : "fx:id=\"chatWindow\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindowScrollPane != null : "fx:id=\"chatWindowScrollPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert composeLabel != null : "fx:id=\"composeLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert createRoomButton != null : "fx:id=\"createRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert databaseTable != null : "fx:id=\"databaseTable\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert fontColorComboBox != null : "fx:id=\"fontColorComboBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert fontSizeComboBox != null : "fx:id=\"fontSizeComboBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert fontTypeComboBox != null : "fx:id=\"fontTypeComboBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gatewayLabel != null : "fx:id=\"gatewayLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gravemindLabel != null : "fx:id=\"gravemindLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert inboxLabel != null : "fx:id=\"inboxLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert italicButton != null : "fx:id=\"italicButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert joinRoomButton != null : "fx:id=\"joinRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert mainPanel != null : "fx:id=\"mainPanel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert menuButton != null : "fx:id=\"menuButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert minimizeButton != null : "fx:id=\"minimizeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert notesLabel != null : "fx:id=\"notesLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert onlineList != null : "fx:id=\"onlineList\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert outboxLabel != null : "fx:id=\"outboxLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert pictureCircle != null : "fx:id=\"pictureCircle\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert rosterLabel != null : "fx:id=\"rosterLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert settingsLabel != null : "fx:id=\"settingsLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert tasksLabel != null : "fx:id=\"tasksLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleBar != null : "fx:id=\"titleBar\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleImageView != null : "fx:id=\"titleImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userAliasContactLabel != null : "fx:id=\"userAliasContactLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userEmailLabel != null : "fx:id=\"userEmailLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userPermissionsLabel != null : "fx:id=\"userPermissionsLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userTitleLabel != null : "fx:id=\"userTitleLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";


    }

}
