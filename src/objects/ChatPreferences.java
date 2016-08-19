package objects;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 8/18/2016.
 */
public final class ChatPreferences {

    public static boolean overrideExternalFonts = false;
    public static Font externalFont;
    public static Font internalFont;
    public static Color externalFontColor;
    public static Color internalFontColor;

    private static double r;
    private static double g;
    private static double b;

    public static List<String> fonts = javafx.scene.text.Font.getFamilies();

    public static Map<String, Color> colorMap = getColors();

    public static boolean isHidden = false;


    public static Map<String, Color> getColors(){
        Map<String, Color> colorMap = new HashMap<>();

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
        return colorMap;
    }

    public static double getR() {
        return internalFontColor.getRed();
    }

    public static void setR(double r) {
        ChatPreferences.r = r;
    }

    public static double getG() {
        return internalFontColor.getGreen();
    }

    public static void setG(double g) {
        ChatPreferences.g = g;
    }

    public static double getB() {
        return internalFontColor.getBlue();
    }

    public static void setB(double b) {
        ChatPreferences.b = b;
    }
}
