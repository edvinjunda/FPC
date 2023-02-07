package fxControllers;

import food.Dish;
import food.Food;
import food.Status;
import hibernateControllers.DishHibernateCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

import static incorrectDataControl.IncorrectDataControl.alertMsg;
import static incorrectDataControl.IncorrectDataControl.containsCharactersInteger;

public class FoodAmountWindow implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private TextField foodInDishAmount;
    @FXML
    private Button setButton;

    Dish tempDish;
    Food tempFood;
    ListView tempDishList;

    public FoodAmountWindow(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void SetAmount(Dish dish, Food food, ListView dishList) {
        tempDish=dish;
        tempFood=food;
        tempDishList=dishList;
    }

    public void setAmount(ActionEvent actionEvent) {
        if (foodInDishAmount.getText().isEmpty()) {
            alertMsg("Field is empty!", "Enter amount/pieces!");
        }

        else if (containsCharactersInteger(foodInDishAmount.getText())) {
            alertMsg("Field contains characters!", "Remove all characters!");
        }

        else {
            tempDish.getAmount().add(Integer.parseInt(foodInDishAmount.getText()));
            tempDish.getIngredients().add(tempFood);
            tempDishList.getItems().add(tempFood.getId() + ":" + tempFood.getName());

            Stage stage = (Stage) setButton.getScene().getWindow();
            stage.close();
        }
    }

    public void cancelAmount(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
