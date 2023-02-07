package incorrectDataControl;

import javafx.scene.control.Alert;

import java.time.LocalDate;

public class IncorrectDataControl {

    public static void alertMsg(String header, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static boolean containsDigits(String word) {
        for(int i=0;i<10;i++)
            if (word.contains(String.valueOf(i)))
                return true;

        return false;
    }

    public static boolean containsCharactersInteger(String number){
        try{
                Integer.parseInt(String.valueOf(number));
        }
        catch (Exception e){
            return true;
        }
        return false;
    }

    public static boolean containsCharactersDouble(String number){
        try{
                Double.parseDouble(String.valueOf(number));
        }
        catch (Exception e){
            return true;
        }
        return false;
    }
}
