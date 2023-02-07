package fxControllers;

import food.Dish;
import food.PortionSize;
import food.Status;
import hibernateControllers.DishHibernateCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

import static incorrectDataControl.IncorrectDataControl.alertMsg;
import static incorrectDataControl.IncorrectDataControl.containsCharactersInteger;

public class FoodNameAndStatusWindow implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button setButton;
    @FXML
    private TextField dishName;
    @FXML
    private ComboBox dishStatus;
    @FXML
    private ComboBox dishPortionSize;

    Dish tempDish;
    public FoodNameAndStatusWindow(){}

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FoodPriceCompiler");
    DishHibernateCtrl dishHibernateCtrl = new DishHibernateCtrl(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dishStatus.getItems().addAll(
                Status.NECESSARY,
                Status.UNNECESSARY,
                Status.REFUSED
           );
        dishPortionSize.getItems().addAll(
                PortionSize.LARGE,
                PortionSize.MEDIUM,
                PortionSize.SMALL
        );
    }

    public void SetNameAndStatus(Dish dish) {
        tempDish=dish;
    }

    public void SetNameAndStatus() {
        tempDish.setName(dishName.getText());
        tempDish.setStatus(dishStatus.getSelectionModel().getSelectedItem()==null
                ? null : Status.valueOf(dishStatus.getSelectionModel().getSelectedItem().toString()));
        tempDish.setPortionSize(dishPortionSize.getSelectionModel().getSelectedItem()==null
                ? null : PortionSize.valueOf(dishPortionSize.getSelectionModel().getSelectedItem().toString()));

        Stage stage = (Stage) setButton.getScene().getWindow();
        stage.close();
    }
}
