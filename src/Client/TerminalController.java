package Client;

import RoomPane.RoomPaneController;
import Server.InformationMessage;
import Server.Room;
import Server.SerializableRoom;
import chat.ChatPreferencesWindow;
import enums.CommandHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import objects.*;
import room_request.CreateRoomWindow;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * Created by Adam on 8/11/2016.
 */

public class TerminalController implements Initializable {


    private ClassLoader classLoader = this.getClass().getClassLoader();

    private boolean expanded = false;

    private Thread translationThread;
    private Thread thread;

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

    private void connect() {

        Thread thread = new Thread(() -> {
            ConnectionHandler.connect();
        });
        thread.start();

    }

    private void setGraphics() {

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

        ImageView menuIcon = new ImageView();
        menuIcon.setImage(new Image(classLoader.getResourceAsStream("Images/menu_icon.png")));
        menuIcon.setFitWidth(25);
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
        commandsImageView.setImage(new Image(classLoader.getResourceAsStream("Images/command_icon.png")));
        historyImageView.setImage(new Image(classLoader.getResourceAsStream("Images/history_icon.png")));

        bannerImageView.setImage(new Image("Images/hackbackground.png"));
        settingsLabel.setImage(new Image("Images/settings_icon.png"));

        ImagePattern imagePattern = new ImagePattern(new Image("Images/anonymous_login_icon.png"));
        pictureCircle.setFill(imagePattern);

        chatSettingsImageView.setImage(new Image("Images/font_icon.png"));
        emailImageView.setImage(new Image("Images/email_icon.png"));
        rosterImageView.setImage(new Image("Images/roster_icon.png"));
        tasksImageView.setImage(new Image("Images/tasks_icon.png"));

        databaseImageView.setImage(new Image("Images/database_icon.png"));
        gravemindImageView.setImage(new Image("Images/gravemind_icon.png"));
        gatewayImageView.setImage(new Image("Images/gateway_icon.png"));
        notesImageView.setImage(new Image("Images/notes_icon.png"));
        commandPromptImageView.setImage(new Image("Images/command_prompt_icon.png"));
        chromeImageView.setImage(new Image("Images/chrome_icon.png"));


        chatTabPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255), CornerRadii.EMPTY, Insets.EMPTY)));
        innerChatPanel.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235), CornerRadii.EMPTY, Insets.EMPTY)));
        onlineListPane.setBackground(new Background(new BackgroundFill(Color.rgb(235, 235, 235), CornerRadii.EMPTY, Insets.EMPTY)));

    }

    private void setPreferences() {

        chatWindowScrollPane.vvalueProperty().bind(chatWindow.heightProperty());

        chatTabPane.toFront();


        emailButton.setTooltip(new Tooltip("Email"));
        rosterButton.setTooltip(new Tooltip("Roster"));
        tasksButton.setTooltip(new Tooltip("Tasks"));
        menuButton.setTooltip(new Tooltip("Menu"));
        databaseButton.setTooltip(new Tooltip("Database"));
        gravemindButton.setTooltip(new Tooltip("Gravemind"));
        gatewayButton.setTooltip(new Tooltip("Gateway"));
        notesButton.setTooltip(new Tooltip("Notes"));
        commandPromptButton.setTooltip(new Tooltip("Command Prompt"));
        chromeButton.setTooltip(new Tooltip("Google Chrome"));


    }



    private void sendCommand(String text) {
        CommandHandler.sendCommand(text);
    }

    private void verifyMessage(String text) {
        if (text.startsWith("-")) {
            sendCommand(text);
        } else {
            Message message = new Message(RoomHandler.getCurrentRoomKey(), MasterClass.user, text, ChatPreferences.internalFont.getFamily(), ChatPreferences.getR(), ChatPreferences.getG(), ChatPreferences.getB(), false);
            sendMessage(message);
        }

    }

    private void createListeners() {

        menuButton.setOnAction(Event -> {
            if (expanded) {
                collapse();
            } else expand();
        });

        titleBar.setOnMousePressed(Event -> {
            xOffset = titleBar.getScene().getWindow().getX() - MouseInfo.getPointerInfo().getLocation().getX();
            yOffset = titleBar.getScene().getWindow().getY() - MouseInfo.getPointerInfo().getLocation().getY();
        });

        titleBar.setOnMouseDragged(Event -> {
            titleBar.getScene().getWindow().setX(MouseInfo.getPointerInfo().getLocation().getX() + xOffset);
            titleBar.getScene().getWindow().setY(MouseInfo.getPointerInfo().getLocation().getY() + yOffset);
        });

        closeButton.setOnAction(event -> closeWindow());

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

        chatSettingsImageView.setOnMouseClicked(Event -> {

            if (Event.getClickCount() == 1) {
                try {
                    Application app = new ChatPreferencesWindow(this);
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

        chatSettingsImageView.setOnMouseEntered(mouseEntered);
        emailButton.setOnMouseEntered(mouseEntered);
        rosterButton.setOnMouseEntered(mouseEntered);
        tasksButton.setOnMouseEntered(mouseEntered);
        databaseButton.setOnMouseEntered(mouseEntered);
        gravemindButton.setOnMouseEntered(mouseEntered);
        gatewayButton.setOnMouseEntered(mouseEntered);
        notesButton.setOnMouseEntered(mouseEntered);
        commandPromptButton.setOnMouseEntered(mouseEntered);
        chromeButton.setOnMouseEntered(mouseEntered);
        commandsImageView.setOnMouseEntered(mouseEntered);
        historyImageView.setOnMouseEntered(mouseEntered);



        chatSettingsImageView.setOnMouseExited(mouseExited);
        emailButton.setOnMouseExited(mouseExited);
        rosterButton.setOnMouseExited(mouseExited);
        tasksButton.setOnMouseExited(mouseExited);
        databaseButton.setOnMouseExited(mouseExited);
        gravemindButton.setOnMouseExited(mouseExited);
        gatewayButton.setOnMouseExited(mouseExited);
        notesButton.setOnMouseExited(mouseExited);
        commandPromptButton.setOnMouseExited(mouseExited);
        chromeButton.setOnMouseExited(mouseExited);
        commandsImageView.setOnMouseExited(mouseExited);
        historyImageView.setOnMouseExited(mouseExited);

        chromeButton.setOnAction(Event ->{



        });

        createRoomButton.setOnAction(Event ->{
            createRoom();
        });

    }

    public void createRoom(){

        try {
            HashSet<User> currentlyOnline = new HashSet<>();
            onlineBox.getChildren().forEach(child ->{
                currentlyOnline.add(((UserLabel) child).getUser());
            });
            Application app = new CreateRoomWindow(currentlyOnline);
            Stage stage = new Stage();
            app.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMessage(Message message) {

        addClientMessage(message);
        ConnectionHandler.sendMessage(message);

    }

    public void clearScreen(int key) {
        Thread thread = new Thread(() -> {
            chatWindow.getChildren().clear();
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);

    }

    public void addInformationMessage(String message) {
        InformationLabel label = new InformationLabel(message);
        chatWindow.getChildren().add(label);

    }

    public void addToChatWindow(Node node) {
        Thread thread = new Thread(() -> {
            chatWindow.getChildren().add(node);
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);
    }

    public void receiveInformationMessage(InformationMessage message) {
        InformationLabel messageLabel = new InformationLabel(message);
        System.out.println(message.getText());
        addToChatWindow(messageLabel);

        switch(message.getType()){
            case USER_JOINED:
                addOnline(message.getUser());
                break;

            case USER_LEFT:

                break;

            case ROOM_CREATED:
                addRoom(message.getRoom());
                RoomHandler.addRoom(message.getRoom());
                break;
        }

    }

    private void addRoom(SerializableRoom room){
        Thread thread = new Thread(() ->{

            try {
                URL url = getClass().getResource("/RoomPane/RoomPaneFXML.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(url);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                AnchorPane page = (AnchorPane) fxmlLoader.load(url.openStream());
                System.out.println(fxmlLoader.getController().getClass());

                Tab newTab = new Tab(room.getTitle());

                RoomPaneController controller = (RoomPaneController) fxmlLoader.getController();
                controller.setKey(room.getKey());

                AnchorPane pane = new AnchorPane();
                pane.setPrefWidth(1230);
                pane.setPrefHeight(515);

                pane.getChildren().add(page);

                page.setLayoutX(12);
                page.setLayoutY(10);

                newTab.setContent(pane);

                chatTabPane.getTabs().add(newTab);
                chatTabPane.getSelectionModel().select(newTab);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
        });

        Platform.runLater(thread);

    }

    private void addOnline(User user) {

        Thread thread = new Thread(() -> {
            onlineBox.getChildren().add(new UserLabel(user));
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);
    }


    private synchronized void expand() {


        menuButton.setDisable(true);
        centerPanel.toBack();

        centerPanel.setDisable(true);
        expanded = true;
        translationThread = new Thread(() -> {

            while (menuPanel.getLayoutX() < 0) {
                try {
                    menuPanel.setLayoutX(menuPanel.getLayoutX() + 1);
                    //if(centerPanel.getOpacity() > 60) {
                    //   centerPanel.setOpacity(centerPanel.getOpacity() - 1);
                    //}
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

    private synchronized void collapse() {

        expanded = false;
        menuButton.setDisable(true);
        translationThread = new Thread(() -> {
            while (menuPanel.getLayoutX() > -(menuPanel.getWidth())) {

                try {
                    menuPanel.setLayoutX(menuPanel.getLayoutX() - 1);
                    if (centerPanel.isDisabled()) {
                        centerPanel.setDisable(false);
                    }
                    if (menuPanel.getLayoutX() == -menuPanel.getWidth()) {
                        Thread temp = new Thread(() -> {
                            //centerPanel.toFront();
                            chatTabPane.toFront();
                            Thread.currentThread().interrupt();
                        });
                        Platform.runLater(temp);
                    }
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            menuPanel.setLayoutX(-(menuPanel.getWidth()));
            menuButton.setDisable(false);
            //centerPanel.setDisable(false);
            Thread.currentThread().interrupt();
            return;

        });
        translationThread.start();
        //centerPanel.toFront();
    }

    public void disable() {
        mainPanel.setDisable(true);
    }

    public void enable() {
        mainPanel.setDisable(false);
    }

    public void addClientMessage(Message message) {


        MessageLabel messageLabel = new MessageLabel(message);

        messageLabel.setBlendMode(BlendMode.EXCLUSION);
        messageLabel.setFont(ChatPreferences.internalFont);
        messageLabel.setTextFill(ChatPreferences.internalFontColor);
        messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
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
            messageLabel.setMaxSize(chatWindow.getWidth() - 25, chatWindow.getHeight());
            messageLabel.setWrapText(true);


            chatWindow.getChildren().add(messageLabel);


        });

        Platform.runLater(thread);

    }


    private void closeWindow() {

        Stage stage = (Stage) mainPanel.getScene().getWindow();
        stage.close();
        System.exit(0);


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
                onlineListLabel.setText("Members Online: [" + h.size() + "]");
            });
            Thread.currentThread().interrupt();
        });
        Platform.runLater(thread);
    }


    //------------------------------------------JAVAFX INITIALIZATION---------------------------------------------------

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane onlineListPane;

    @FXML
    private Label archiveLabel;

    @FXML
    private Label onlineListLabel;

    @FXML
    private ToolBar displayBar;

    @FXML
    private ImageView attachmentImageView;

    @FXML
    private ImageView bannerImageView;

    @FXML
    private AnchorPane bannerPane;

    @FXML
    private AnchorPane centerPanel;

    @FXML
    private TextField chatLine;

    @FXML
    private ImageView chatSettingsImageView;

    @FXML
    private TabPane chatTabPane;

    @FXML
    private VBox chatWindow;

    @FXML
    private AnchorPane chatWindowPane;

    @FXML
    private ScrollPane chatWindowScrollPane;

    @FXML
    private Button chromeButton;

    @FXML
    private ImageView chromeImageView;

    @FXML
    private Button closeButton;

    @FXML
    private AnchorPane innerChatPanel;

    @FXML
    private Button commandPromptButton;

    @FXML
    private ImageView commandPromptImageView;

    @FXML
    private ImageView commandsImageView;

    @FXML
    private Label composeLabel;

    @FXML
    private Button createRoomButton;

    @FXML
    private Button databaseButton;

    @FXML
    private ImageView databaseImageView;

    @FXML
    private Label databaseTable;

    @FXML
    private Button emailButton;

    @FXML
    private ImageView emailImageView;

    @FXML
    private Button gatewayButton;

    @FXML
    private ImageView gatewayImageView;

    @FXML
    private Label gatewayLabel;

    @FXML
    private Button gravemindButton;

    @FXML
    private ImageView gravemindImageView;

    @FXML
    private Label gravemindLabel;

    @FXML
    private ImageView historyImageView;

    @FXML
    private Label inboxLabel;

    @FXML
    private Button joinRoomButton;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private AnchorPane menuBodyPane;

    @FXML
    private Button menuButton;

    @FXML
    private SplitPane menuPanel;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button notesButton;

    @FXML
    private ImageView notesImageView;

    @FXML
    private Label notesLabel;

    @FXML
    private VBox onlineBox;

    @FXML
    private Label outboxLabel;

    @FXML
    private Circle pictureCircle;

    @FXML
    private Button rosterButton;

    @FXML
    private ImageView rosterImageView;

    @FXML
    private Label rosterLabel;

    @FXML
    private ImageView settingsLabel;

    @FXML
    private Button tasksButton;

    @FXML
    private ImageView tasksImageView;

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
        assert bannerImageView != null : "fx:id=\"bannerImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert bannerPane != null : "fx:id=\"bannerPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert centerPanel != null : "fx:id=\"centerPanel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatLine != null : "fx:id=\"chatLine\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatSettingsImageView != null : "fx:id=\"chatSettingsImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatTabPane != null : "fx:id=\"chatTabPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindow != null : "fx:id=\"chatWindow\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindowPane != null : "fx:id=\"chatWindowPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chatWindowScrollPane != null : "fx:id=\"chatWindowScrollPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chromeButton != null : "fx:id=\"chromeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert chromeImageView != null : "fx:id=\"chromeImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert commandPromptButton != null : "fx:id=\"commandPromptButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert commandPromptImageView != null : "fx:id=\"commandPromptImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert commandsImageView != null : "fx:id=\"commandsImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert composeLabel != null : "fx:id=\"composeLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert createRoomButton != null : "fx:id=\"createRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert databaseButton != null : "fx:id=\"databaseButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert databaseImageView != null : "fx:id=\"databaseImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert databaseTable != null : "fx:id=\"databaseTable\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert emailButton != null : "fx:id=\"emailButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert emailImageView != null : "fx:id=\"emailImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gatewayButton != null : "fx:id=\"gatewayButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gatewayImageView != null : "fx:id=\"gatewayImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gatewayLabel != null : "fx:id=\"gatewayLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gravemindButton != null : "fx:id=\"gravemindButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gravemindImageView != null : "fx:id=\"gravemindImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert gravemindLabel != null : "fx:id=\"gravemindLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert historyImageView != null : "fx:id=\"historyImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert inboxLabel != null : "fx:id=\"inboxLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert joinRoomButton != null : "fx:id=\"joinRoomButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert mainPanel != null : "fx:id=\"mainPanel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert menuBodyPane != null : "fx:id=\"menuBodyPane\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert menuButton != null : "fx:id=\"menuButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert menuPanel != null : "fx:id=\"menuPanel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert minimizeButton != null : "fx:id=\"minimizeButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert notesButton != null : "fx:id=\"notesButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert notesImageView != null : "fx:id=\"notesImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert notesLabel != null : "fx:id=\"notesLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert onlineBox != null : "fx:id=\"onlineBox\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert outboxLabel != null : "fx:id=\"outboxLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert pictureCircle != null : "fx:id=\"pictureCircle\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert rosterButton != null : "fx:id=\"rosterButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert rosterImageView != null : "fx:id=\"rosterImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert rosterLabel != null : "fx:id=\"rosterLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert settingsLabel != null : "fx:id=\"settingsLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert tasksButton != null : "fx:id=\"tasksButton\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert tasksImageView != null : "fx:id=\"tasksImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert tasksLabel != null : "fx:id=\"tasksLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleBar != null : "fx:id=\"titleBar\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert titleImageView != null : "fx:id=\"titleImageView\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userAliasContactLabel != null : "fx:id=\"userAliasContactLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userEmailLabel != null : "fx:id=\"userEmailLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userPermissionsLabel != null : "fx:id=\"userPermissionsLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";
        assert userTitleLabel != null : "fx:id=\"userTitleLabel\" was not injected: check your FXML file 'TerminalFXML.fxml'.";

    }
}
