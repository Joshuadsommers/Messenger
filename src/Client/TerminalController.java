package Client;

import Server.Room;
import chat.ChatPreferencesWindow;
import chat.FontLabel;
import enums.CommandHandler;
import javafx.application.Application;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import objects.ChatPreferences;
import objects.MasterClass;
import objects.Message;
import objects.MessageLabel;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
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



    private int fontSize = 14;
    private int fontStyle;
    private double xOffset;
    private double yOffset;


    ArrayList<Label> history = new ArrayList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MasterClass.client = this;
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

        bannerImageView.setImage(new Image("Images/hackbackground.png"));
        settingsLabel.setImage(new Image("Images/settings_icon.png"));
        ImagePattern imagePattern = new ImagePattern(new Image("Images/anonymous_login_icon.png"));
        pictureCircle.setFill(imagePattern);

        chatSettingsImageView.setImage(new Image("Images/menu_icon.png"));

    }

    private void setPreferences(){

        chatWindowScrollPane.vvalueProperty().bind(chatWindow.heightProperty());

        fontSizeComboBox.getItems().clear();
        fontSizeComboBox.getItems().add(14);
        fontSizeComboBox.getItems().add(16);
        fontSizeComboBox.getItems().add(18);


        setFontColorBox();
        setFontComboBox();


        ChatPreferences.internalFont = new javafx.scene.text.Font(fontType, 14);
        ChatPreferences.internalFontColor = fontColorComboBox.getSelectionModel().getSelectedItem().getColor();

        connectionHandler = new ConnectionHandler(MasterClass.user, this);

    }


    private void setFontColorBox(){
        fontColorComboBox.getItems().clear();
        ChatPreferences.colorMap.keySet().forEach(color ->{
            fontColorComboBox.getItems().add(new FontLabel(ChatPreferences.colorMap.get(color), color));
        });

        fontColorComboBox.getSelectionModel().select(0);
        fontColorComboBox.getSelectionModel().getSelectedItem().setBlendMode(BlendMode.EXCLUSION);


    }

    private void setFontComboBox(){
        fontTypeComboBox.getItems().clear();
        ChatPreferences.fonts.forEach(font ->{
            FontLabel label = new FontLabel(font);
            label.setBlendMode(BlendMode.EXCLUSION);
            fontTypeComboBox.getItems().add(label);
        });
        fontTypeComboBox.getSelectionModel().select(0);
        fontTypeComboBox.getSelectionModel().getSelectedItem();

    }

    private void verifyMessage(String text){
        if(text.startsWith("-")){
            handleLocalCommand(text);
        }
        else{
            Message message = new Message(RoomHandler.getCurrentRoom(), MasterClass.user, text, ChatPreferences.internalFont.getFamily(), ChatPreferences.getR(), ChatPreferences.getG(), ChatPreferences.getB(), false);
            sendMessage(message);
        }

    }

    private void handleLocalCommand(String command){
        CommandHandler.handleCommand(command);
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
                verifyMessage(text);
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
            fontColorComboBox.getSelectionModel().getSelectedItem().setBlendMode(BlendMode.EXCLUSION);
            ChatPreferences.internalFontColor = fontColorComboBox.getSelectionModel().getSelectedItem().getColor();
        });

        fontTypeComboBox.setOnAction(Event ->{
            ChatPreferences.internalFont = new javafx.scene.text.Font(fontTypeComboBox.getSelectionModel().getSelectedItem().getFamily(), fontSize);
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

        chatSettingsImageView.setOnMouseClicked(Event ->{

            if(Event.getClickCount() == 2){
                try {
                    Application app = new ChatPreferencesWindow(this);
                    Stage stage = new Stage();
                    app.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        chatSettingsImageView.setOnMouseEntered(Event ->{
            chatSettingsImageView.setScaleX(1.3);
            chatSettingsImageView.setScaleY(1.3);
            chatSettingsImageView.setBlendMode(BlendMode.EXCLUSION);
        });

        chatSettingsImageView.setOnMouseExited(Event ->{
            chatSettingsImageView.setScaleX(1);
            chatSettingsImageView.setScaleY(1);
            chatSettingsImageView.setBlendMode(null);
        });



    }

    public void updateFonts(int familyIndex, int colorIndex){
        fontTypeComboBox.getSelectionModel().select(familyIndex);
        fontColorComboBox.getSelectionModel().select(colorIndex);
    }

    private void sendMessage(Message message){

        addClientMessage(message);
        connectionHandler.sendMessage(message);

    }

    public void clearScreen(){
        chatWindow.getChildren().clear();
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
            menuPanel.setLayoutX(0);
            menuButton.setDisable(false);
            Thread.currentThread().interrupt();
            return;

        });
        translationThread.start();
    }

    private synchronized void collapse(){

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
            menuPanel.setLayoutX(-(menuPanel.getWidth()));
            menuButton.setDisable(false);
            centerPanel.setDisable(false);
            Thread.currentThread().interrupt();
            return;

        });
        translationThread.start();
        centerPanel.toFront();
    }

    public void disable(){
        mainPanel.setDisable(true);
    }

    public void enable(){
        mainPanel.setDisable(false);
    }

    public void addClientMessage(Message message){

        thread = new Thread(() ->{

            MessageLabel messageLabel = new MessageLabel(message);

            messageLabel.setBlendMode(BlendMode.EXCLUSION);
            messageLabel.setFont(ChatPreferences.internalFont);
            messageLabel.setTextFill(ChatPreferences.internalFontColor);
            messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
            messageLabel.setWrapText(true);


            chatWindow.getChildren().add(messageLabel);

        });

        Platform.runLater(thread);

    }

    // Appends the message to the area for chat in the client.
    // Method is called from receive Input when a message is received from the server
    // or when we append to the client before sending to the server
    public void appendMessage(Message message){

        thread = new Thread(() ->{

            MessageLabel messageLabel = new MessageLabel(message);

            messageLabel.setBlendMode(BlendMode.EXCLUSION);
            // messageLabel.setFont(new javafx.scene.text.Font(fontType, fontSize));
            if(ChatPreferences.overrideExternalFonts) {
                messageLabel.setFont(ChatPreferences.externalFont);
                messageLabel.setTextFill(ChatPreferences.externalFontColor);
            }
            else{
                messageLabel.setFont(new javafx.scene.text.Font(message.getFont(), fontSize));
                messageLabel.setTextFill(Color.color(message.getR(), message.getG(), message.getB()));
            }
            //messageLabel.setTextFill(fontColor);
            messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
            //messageLabel.setStyle("-fx-font-weight: bold;"); // MAKES FONT BOLD
            messageLabel.setWrapText(true);


            chatWindow.getChildren().add(messageLabel);


            /*
            MessageLabel messageLabel = new MessageLabel(message);

            messageLabel.setBlendMode(BlendMode.EXCLUSION);
            messageLabel.setFont(new javafx.scene.text.Font(fontType, fontSize));
            messageLabel.setTextFill(fontColor);
            messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
            //messageLabel.setStyle("-fx-font-weight: bold;"); // MAKES FONT BOLD
            messageLabel.setWrapText(true);


            chatWindow.getChildren().add(messageLabel);
            */
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
    private ImageView chatSettingsImageView;

    @FXML
    private AnchorPane bannerPane;

    @FXML
    private ImageView bannerImageView;
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
    private ComboBox<FontLabel> fontColorComboBox;

    @FXML
    private ComboBox<Integer> fontSizeComboBox;

    @FXML
    private ComboBox<FontLabel> fontTypeComboBox;

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
