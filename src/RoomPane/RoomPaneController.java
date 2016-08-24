package RoomPane;

import Client.ConnectionHandler;
import Client.RoomHandler;
import Server.InformationMessage;
import chat.ChatPreferencesWindow;
import enums.CommandHandler;
import enums.InformationType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import objects.*;
import room_request.CreateRoomWindow;

import java.awt.*;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Created by Adam on 8/23/2016.
 */
public class RoomPaneController implements Initializable {

    private ClassLoader classLoader = this.getClass().getClassLoader();
    private int roomKey;
    private Thread thread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGraphics();
        setPreferences();
        createListeners();
    }

    private void setGraphics(){

        mainPanel.setBlendMode(null);

        ImageView chatIcon = new ImageView();
        chatIcon.setImage(new Image(classLoader.getResourceAsStream("Images/chat_icon.png")));
        chatIcon.setFitWidth(15);
        chatIcon.setFitHeight(15);
        chatIcon.setBlendMode(BlendMode.EXCLUSION);
        joinRoomButton.setGraphic(chatIcon);

        ImageView addIcon = new ImageView();
        addIcon.setImage(new Image(classLoader.getResourceAsStream("Images/add_icon.png")));
        addIcon.setFitWidth(15);
        addIcon.setFitHeight(15);
        addIcon.setBlendMode(BlendMode.EXCLUSION);
        createRoomButton.setGraphic(addIcon);

        ImageView leaveIcon = new ImageView();
        leaveIcon.setImage(new Image(classLoader.getResourceAsStream("Images/leave_icon.png")));
        leaveIcon.setFitWidth(15);
        leaveIcon.setFitHeight(15);
        leaveRoomButton.setGraphic(leaveIcon);

        attachmentImageView.setImage(new Image(classLoader.getResourceAsStream("Images/attachmentIcon.png")));
        commandsImageView.setImage(new Image(classLoader.getResourceAsStream("Images/command_icon.png")));
        fontImageView.setImage(new Image(classLoader.getResourceAsStream("Images/font_icon.png")));
        historyImageView.setImage(new Image(classLoader.getResourceAsStream("Images/history_icon.png")));

        chatPanel.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235), CornerRadii.EMPTY, Insets.EMPTY)));
        onlineListPane.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setPreferences(){
        chatWindowScrollPane.vvalueProperty().bind(chatBox.heightProperty());
    }

    private void createListeners(){

        chatLine.setOnAction(Event -> {

            String text = chatLine.getText();

            if (text.length() > 0) {
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

        fontImageView.setOnMouseClicked(Event -> {

            if (Event.getClickCount() == 1) {
                try {
                    Application app = new ChatPreferencesWindow(MasterClass.client);
                    Stage stage = new Stage();
                    app.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });




        EventHandler<MouseEvent> mouseEntered = mouseEvent -> {
            Node source = (Node) mouseEvent.getSource();
            source.setScaleX(1.2);
            source.setScaleY(1.2);
        };

        EventHandler<MouseEvent> mouseExited = mouseEvent -> {
            Node source = (Node) mouseEvent.getSource();
            source.setScaleX(1);
            source.setScaleY(1);
        };
        commandsImageView.setOnMouseEntered(mouseEntered);
        historyImageView.setOnMouseEntered(mouseEntered);



        commandsImageView.setOnMouseExited(mouseExited);
        historyImageView.setOnMouseExited(mouseExited);



        createRoomButton.setOnAction(Event ->{
            createRoom();
        });

    }

    private void createRoom(){

        try {
            Application app = new CreateRoomWindow();
            Stage stage = new Stage();
            app.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearScreen(int key) {
        Thread thread = new Thread(() -> {
            chatBox.getChildren().clear();
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);

    }

    public void setKey(int roomKey){
       this.roomKey = roomKey;
    }

    private void sendCommand(String text) {
        CommandHandler.sendCommand(text);
    }

    private void verifyMessage(String text) {
        if (text.startsWith("-")) {
            sendCommand(text);
        } else {
            Message message = new Message(roomKey, MasterClass.user, text, ChatPreferences.internalFont.getFamily(), ChatPreferences.getR(), ChatPreferences.getG(), ChatPreferences.getB(), false);
            sendMessage(message);
        }
    }

    private void sendMessage(Message message) {

        addClientMessage(message);
        ConnectionHandler.sendMessage(message);

    }

    public void addInformationMessage(String message) {
        InformationLabel label = new InformationLabel(message);
        chatBox.getChildren().add(label);

    }

    public void addToChatWindow(Node node) {
        Thread thread = new Thread(() -> {
            chatBox.getChildren().add(node);
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);
    }

    public void receiveInformationMessage(InformationMessage message) {
        InformationLabel messageLabel = new InformationLabel(message);
        System.out.println(message.getText());
        addToChatWindow(messageLabel);

        if (message.getType().equals(InformationType.USER_JOINED)) addOnline(message.getUser());

    }

    public void addOnline(User user) {

        Thread thread = new Thread(() -> {
            onlineBox.getChildren().add(new UserLabel(user));
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);
    }


    public void addClientMessage(Message message) {


        MessageLabel messageLabel = new MessageLabel(message);

        messageLabel.setBlendMode(BlendMode.EXCLUSION);
        messageLabel.setFont(ChatPreferences.internalFont);
        messageLabel.setTextFill(ChatPreferences.internalFontColor);
        messageLabel.setMaxSize(chatBox.getWidth() - 25, chatBox.getHeight());
        messageLabel.setWrapText(true);


        addToChatWindow(messageLabel);


    }

    // Appends the message to the area for chat in the client.
    // Method is called from receive Input when a message is received from the server
    // or when we append to the client before sending to the server
    public void appendMessage(Message message) {

        thread = new Thread(() -> {

            MessageLabel messageLabel = new MessageLabel(message);

            messageLabel.setBlendMode(BlendMode.EXCLUSION);
            if (ChatPreferences.overrideExternalFonts) {
                messageLabel.setFont(ChatPreferences.externalFont);
                messageLabel.setTextFill(ChatPreferences.externalFontColor);
            } else {
                messageLabel.setFont(new javafx.scene.text.Font(message.getFont(), ChatPreferences.fontSize));
                messageLabel.setTextFill(Color.color(message.getR(), message.getG(), message.getB()));
            }
            messageLabel.setMaxSize(chatBox.getWidth() - 25, chatBox.getHeight());
            messageLabel.setWrapText(true);


            chatBox.getChildren().add(messageLabel);


        });

        Platform.runLater(thread);

    }




    public void append(InformationMessage message) {
        InformationLabel label = new InformationLabel(message);
        label.setAlignment(Pos.CENTER);
        addToChatWindow(label);
    }

    public void updateList(HashSet h) {
        Thread thread = new Thread(() -> {
            onlineBox.getChildren().clear();
            h.forEach(value -> {
                onlineBox.getChildren().add(new UserLabel((User) value));
                membersPresentLabel.setText("Members Present: [" + h.size() + "]");
            });
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);
    }


    //-----------------------------------------JAVAFX INITIALIZATION----------------------------------------------------

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView attachmentImageView;

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane chatWindowScrollPane;

    @FXML
    private TextField chatLine;

    @FXML
    private AnchorPane chatPanel;

    @FXML
    private ImageView commandsImageView;

    @FXML
    private Button createRoomButton;

    @FXML
    private ImageView fontImageView;

    @FXML
    private ImageView historyImageView;

    @FXML
    private Button joinRoomButton;

    @FXML
    private Label keyLabel;

    @FXML
    private Button leaveRoomButton;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Label membersPresentLabel;

    @FXML
    private VBox onlineBox;

    @FXML
    private AnchorPane onlineListPane;


    @FXML
    void initialize() {
        assert attachmentImageView != null : "fx:id=\"attachmentImageView\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert chatBox != null : "fx:id=\"chatBox\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert chatLine != null : "fx:id=\"chatLine\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert chatPanel != null : "fx:id=\"chatPanel\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert commandsImageView != null : "fx:id=\"commandsImageView\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert createRoomButton != null : "fx:id=\"createRoomButton\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert fontImageView != null : "fx:id=\"fontImageView\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert historyImageView != null : "fx:id=\"historyImageView\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert joinRoomButton != null : "fx:id=\"joinRoomButton\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert keyLabel != null : "fx:id=\"keyLabel\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert leaveRoomButton != null : "fx:id=\"leaveRoomButton\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert mainPanel != null : "fx:id=\"mainPanel\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert membersPresentLabel != null : "fx:id=\"membersPresentLabel\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert onlineBox != null : "fx:id=\"onlineBox\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";
        assert onlineListPane != null : "fx:id=\"onlineListPane\" was not injected: check your FXML file 'RoomPaneFXML.fxml'.";


    }

}
