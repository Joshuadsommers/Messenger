package chat;

import Client.TerminalController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import objects.ChatPreferences;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Adam on 8/18/2016.
 */
public class ChatPreferencesController implements Initializable {

    private ClassLoader classLoader = this.getClass().getClassLoader();
    private TerminalController controller;

    private Font outboundFont;
    private Font inboundFont;
    private String inboundFontFamily;
    private String outboundFontFamily;
    private Color inboundFontColor;
    private Color outboundFontColor;
    private int familyIndex = 0;
    private int colorIndex = 0;
    private boolean override;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGraphics();
        setPreferences();
        createListeners();

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
    }

    private void setPreferences(){


        ChatPreferences.fonts.forEach(font ->{
            FontLabel inboundFontLabel = new FontLabel(font);
            FontLabel outboundFontLabel = new FontLabel(font);
            inboundFontFamilyBox.getChildren().add(inboundFontLabel);
            outboundFontFamilyBox.getChildren().add(outboundFontLabel);
        });

        ChatPreferences.colorMap.keySet().forEach(color ->{
            FontLabel inboundColorLabel = new FontLabel(ChatPreferences.colorMap.get(color), color);
            FontLabel outboundColorLabel = new FontLabel(ChatPreferences.colorMap.get(color), color);
            inboundColorLabel.setBlendMode(BlendMode.EXCLUSION);
            outboundColorLabel.setBlendMode(BlendMode.EXCLUSION);
            inboundFontColorBox.getChildren().add(inboundColorLabel);
            outboundFontColorBox.getChildren().add(outboundColorLabel);
        });

        defaults();
        override();

    }

    private void defaults(){

        FontLabel outboundFamilyLabel = ((FontLabel) outboundFontFamilyBox.getChildren().get(0));

        String outFamily = outboundFamilyLabel.getFamily();
        outboundFontFamily = outFamily;
        outFontFamilyDisplayField.setText(outFamily);
        outboundFont = new Font(outFamily, 14);
        outboundPreviewLabel.setFont(outboundFont);
        outFontFamilyDisplayField.setFont(outboundFont);


        FontLabel outboundColorLabel = ((FontLabel) outboundFontColorBox.getChildren().get(0));

        Color outColor = outboundColorLabel.getColor();
        outboundFontColor = outColor;
        outboundFontColorDisplayField.setText(outboundColorLabel.getColorName());
        outboundPreviewLabel.setTextFill(outColor);


        FontLabel inboundFamilyLabel = ((FontLabel) inboundFontFamilyBox.getChildren().get(0));
        String inFamily = inboundFamilyLabel.getFamily();
        inboundFontFamily = inFamily;
        inboundFontDisplayField.setText(inFamily);
        inboundFont =  new Font(inFamily, 14);
        inboundPreviewLabel.setFont(inboundFont);
        inboundFontDisplayField.setFont(inboundFont);


        FontLabel inboundColorLabel = (FontLabel) inboundFontColorBox.getChildren().get(0);
        Color color = inboundColorLabel.getColor();
        inboundFontColor = color;
        inboundFontColorDisplayField.setText(inboundColorLabel.getColorName());
        inboundPreviewLabel.setTextFill(color);

    }


    private void createListeners(){
        outboundFontFamilyBox.getChildren().forEach(child ->{
            child.setOnMouseClicked(Event ->{
                if(Event.getClickCount() == 2){
                    String family = ((FontLabel) child).getFamily();
                    outboundFontFamily = family;
                    outFontFamilyDisplayField.setText(family);
                    outboundFont = new Font(family, 14);
                    outboundPreviewLabel.setFont(outboundFont);
                    outFontFamilyDisplayField.setFont(outboundFont);
                }
            });
        });

        outboundFontColorBox.getChildren().forEach(child ->{
            child.setOnMouseClicked(Event ->{
                if(Event.getClickCount() == 2){
                    Color color = ((FontLabel) child).getColor();
                    outboundFontColor = color;
                    outboundFontColorDisplayField.setText(((FontLabel) child).getColorName());
                    outboundPreviewLabel.setTextFill(color);
                }
            });
        });

        inboundFontFamilyBox.getChildren().forEach(child ->{
            child.setOnMouseClicked(Event ->{
                if(Event.getClickCount() == 2){
                    String family = ((FontLabel) child).getFamily();
                    inboundFontFamily = family;
                    inboundFontDisplayField.setText(family);
                    inboundFont =  new Font(family, 14);
                    inboundPreviewLabel.setFont(inboundFont);
                    inboundFontDisplayField.setFont(inboundFont);
                }
            });
        });

        inboundFontColorBox.getChildren().forEach(child ->{
            child.setOnMouseClicked(Event ->{
                if(Event.getClickCount() == 2){
                    Color color = ((FontLabel) child).getColor();
                    inboundFontColor = color;
                    inboundFontColorDisplayField.setText(((FontLabel) child).getColorName());
                    inboundPreviewLabel.setTextFill(color);
                }
            });
        });

        overrideCheckBox.setOnAction(Event ->{
            override = overrideCheckBox.isSelected();
            override();
        });

        closeButton.setOnAction(Event -> closeWindow());
        saveChangesButton.setOnAction(Event -> saveAndClose());

    }

    public void start(TerminalController controller){
        this.controller = controller;
        controller.disable();
    }

    private void override(){
        inboundFontDisplayField.setDisable(!override);
        inboundFontFamilyBox.setDisable(!override);
        inboundFontColorBox.setDisable(!override);
        inboundFontColorDisplayField.setDisable(!override);
    }

    private void saveAndClose(){
        ChatPreferences.overrideExternalFonts = override;
        ChatPreferences.internalFont = outboundFont;
        ChatPreferences.externalFont = inboundFont;
        ChatPreferences.internalFontColor = outboundFontColor;
        ChatPreferences.externalFontColor = inboundFontColor;

        outboundFontFamilyBox.getChildren().forEach(child ->{
            String family = ((FontLabel) child).getFamily();
            if(family.equals(outboundFontFamily)) familyIndex = outboundFontFamilyBox.getChildren().indexOf(child);
        });

        outboundFontColorBox.getChildren().forEach(child ->{
            Color color = ((FontLabel) child).getColor();
            if(color.equals(outboundFontColor)) colorIndex = outboundFontColorBox.getChildren().indexOf(child);
        });

       // controller.updateFonts(familyIndex, colorIndex);
        closeWindow();
    }

    private void closeWindow(){
        controller.enable();
        ((Stage) mainPanel.getScene().getWindow()).close();
    }






    //---------------------------------------------------JAVAFX INITIALIZATION------------------------------------------


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button closeButton;

    @FXML
    private VBox inboundFontFamilyBox;

    @FXML
    private VBox inboundFontColorBox;

    @FXML
    private TextField inboundFontColorDisplayField;

    @FXML
    private TextField inboundFontDisplayField;

    @FXML
    private Label inboundPreviewLabel;

    @FXML
    private AnchorPane mainPanel;

    @FXML
    private Button minimizeButton;

    @FXML
    private TextField outFontFamilyDisplayField;

    @FXML
    private VBox outboundFontColorBox;

    @FXML
    private TextField outboundFontColorDisplayField;

    @FXML
    private VBox outboundFontFamilyBox;

    @FXML
    private Label outboundPreviewLabel;

    @FXML
    private CheckBox overrideCheckBox;

    @FXML
    private Button saveChangesButton;

    @FXML
    private ImageView titleImageView;


    @FXML
    void initialize() {
        assert closeButton != null : "fx:id=\"closeButton\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert inboundFontColorDisplayField != null : "fx:id=\"inboundFontColorDisplayField\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert inboundFontDisplayField != null : "fx:id=\"inboundFontDisplayField\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert inboundPreviewLabel != null : "fx:id=\"inboundPreviewLabel\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert mainPanel != null : "fx:id=\"mainPanel\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert minimizeButton != null : "fx:id=\"minimizeButton\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert outFontFamilyDisplayField != null : "fx:id=\"outFontFamilyDisplayField\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert outboundFontColorBox != null : "fx:id=\"outboundFontColorBox\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert outboundFontColorDisplayField != null : "fx:id=\"outboundFontColorDisplayField\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert outboundFontFamilyBox != null : "fx:id=\"outboundFontFamilyBox\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert outboundPreviewLabel != null : "fx:id=\"outboundPreviewLabel\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert overrideCheckBox != null : "fx:id=\"overrideCheckBox\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert saveChangesButton != null : "fx:id=\"saveChangesButton\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";
        assert titleImageView != null : "fx:id=\"titleImageView\" was not injected: check your FXML file 'ChatPreferencesFXML.fxml'.";


    }

}
