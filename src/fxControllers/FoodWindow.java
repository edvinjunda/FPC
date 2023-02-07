package fxControllers;

import food.Dish;
import food.Food;
import food.PortionSize;
import food.Status;
import hibernateControllers.DishHibernateCtrl;
import hibernateControllers.FoodHibernateCtrl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.sampled.Port;
import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static incorrectDataControl.IncorrectDataControl.*;

public class FoodWindow implements Initializable {

//----------------------------------------------FOOD LIST VARIABLES START---------------------------------------------//
    @FXML
    private Button addFoodButton;
    @FXML
    private ListView allFoodsList;
    @FXML
    private ListView currentDishList;
    @FXML
    private TextArea foodNotesList;
    @FXML
    private TextField nameList;
    @FXML
    private TextField partsAmountList;
    @FXML
    private TextField priceList;
    @FXML
    private TextField searchNameList;
    @FXML
    private ComboBox searchStatusList;

    @FXML
    private TextField statusList;
//-----------------------------------------------FOOD LIST VARIABLES END----------------------------------------------//
//-------------------------------------------FOOD MANAGEMENT VARIABLES START------------------------------------------//
    @FXML
    private ListView foodManagementList;
    @FXML
    private TextField foodNameManagement;
    @FXML
    private TextArea foodNotesManagement;
    @FXML
    private TextField foodPartsAmountManagement;
    @FXML
    private TextField foodPriceManagement;
    @FXML
    private ComboBox foodStatusManagement;
    @FXML
    private TextField searchFoodNameManagement;
    @FXML
    private ComboBox searchFoodStatusManagement;
    @FXML
    private Button updateFoodButton;
//--------------------------------------------FOOD MANAGEMENT VARIABLES END-------------------------------------------//
//-------------------------------------------DISH MANAGEMENT VARIABLES START------------------------------------------//
    @FXML
    private ListView dishFoodList;
    @FXML
    private ListView dishManagementList;
    @FXML
    private TextField dishNameManagement;
    @FXML
    private ComboBox dishStatusManagement;
    @FXML
    private ComboBox dishPortionSizeManagement;
    @FXML
    private ComboBox searchDishStatus;
    @FXML
    private ComboBox searchDishPortionSize;
    @FXML
    private TextField searchDishName;
    @FXML
    private TextField foodInDishAmount;
    @FXML
    private  Button changeAmountInDishButton;
    @FXML
    private Button updateDishButton;
//--------------------------------------------DISH MANAGEMENT VARIABLES END-------------------------------------------//

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FoodPriceCompiler");
    FoodHibernateCtrl foodHibernateCtrl = new FoodHibernateCtrl(entityManagerFactory);
    DishHibernateCtrl dishHibernateCtrl = new DishHibernateCtrl(entityManagerFactory);

    private Dish dish;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //updateFoodButton.setDisable(true);
        //addFoodButton.setDisable(false);
        SetStatusForComboBox(searchStatusList);
        SetStatusForComboBox(searchFoodStatusManagement);
        SetStatusForComboBox(foodStatusManagement);
        SetStatusForComboBox(searchDishStatus);
        SetStatusForComboBox(dishStatusManagement);

        SetPortionSizeForComboBox(searchDishPortionSize);
        SetPortionSizeForComboBox(dishPortionSizeManagement);

        dish=new Dish(new ArrayList<>(), new ArrayList<>());
    }

    public void SetStatusForComboBox(ComboBox box){
        box.getItems().addAll(
                null,
                Status.NECESSARY,
                Status.UNNECESSARY,
                Status.REFUSED);
    }

    public void SetPortionSizeForComboBox(ComboBox box){
        box.getItems().addAll(
                null,
                PortionSize.SMALL,
                PortionSize.MEDIUM,
                PortionSize.LARGE);
    }
//------------------------------------------------FOOD LIST TAB START-------------------------------------------------//

    //------------------------------------------------TEMP-------------------------------------------------//
    public void searchFood(ListView foodList, TextField name, ComboBox status) {
        foodList.getItems().clear();

        Status tempStatus=status.getSelectionModel().getSelectedItem()==null
                ? null : Status.valueOf(status.getSelectionModel().getSelectedItem().toString());

        List<Food> filteredFoods = foodHibernateCtrl.getFilteredFoods(name.getText(),tempStatus);

        filteredFoods.forEach(food -> foodList.getItems().add(food.getId() + ":" + food.getName()));
    }
    //------------------------------------------------TEMP-------------------------------------------------//

    public void SearchFoodList(){
        nameList.clear();
        priceList.clear();
        partsAmountList.clear();
        statusList.clear();
        foodNotesList.clear();

        searchFood(allFoodsList, searchNameList, searchStatusList);
    }

    public void ViewFoodInfo(){
        Food currentFood = getFoodById(allFoodsList.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentFood ==null){
            alertMsg("This food is unavailable.","It was removed.");
        }
        else {
            nameList.setText(currentFood.getName());
            priceList.setText(String.valueOf(currentFood.getPrice()));
            partsAmountList.setText(String.valueOf(currentFood.getPartsAmount()));
            foodNotesList.setText(currentFood.getNotes());
            statusList.setText(currentFood.getStatus().toString());
        }
    }

//
    private Food getFoodById(String id) {
        return foodHibernateCtrl.getFoodById(Integer.parseInt(id));
    }

    public void AddToDish() {
        Food currentFood = getFoodById(allFoodsList.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentFood ==null){
            alertMsg("This food is unavailable.","It was removed.");
        }
        else {
            try {
                loadFoodAmountWindow(dish,currentFood,currentDishList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void CreateDish() {
        if(dish.getAmount().isEmpty()&&dish.getIngredients().isEmpty()){
            alertMsg("Dish is empty!","Add food to the dish.");
        }

        else {
            try {
                loadFoodNameAndStatusWindow(dish);
            } catch (IOException e) {
                e.printStackTrace();
            }

            dishHibernateCtrl.createDish(dish);
            dish = new Dish(new ArrayList<>(), new ArrayList<>());
            currentDishList.getItems().clear();
        }
    }

    public void loadFoodAmountWindow(Dish dish, Food food, ListView dishList) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(FoodWindow.class.getResource("../view/FoodAmountWindow.fxml"));
            Parent parent = fxmlLoader.load();
            FoodAmountWindow foodAmountWindow = fxmlLoader.getController();
            foodAmountWindow.SetAmount(dish, food, dishList);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Set amount for: "+food.getName());
            stage.setScene(scene);
            stage.showAndWait();
    }

    public void loadFoodNameAndStatusWindow(Dish dish) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FoodWindow.class.getResource("../view/FoodNameAndStatusWindow.fxml"));
        Parent parent = fxmlLoader.load();
        FoodNameAndStatusWindow foodNameAndStatusWindow = fxmlLoader.getController();
        foodNameAndStatusWindow.SetNameAndStatus(dish);
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Set name, status an portion size");
            stage.setScene(scene);
            stage.showAndWait();
    }

    public void RemoveFromDish(){
        for(int j = 0; j < dish.getIngredients().size(); j++)
        {
            Food tempFood = dish.getIngredients().get(j);
            if(tempFood.getId() == Integer.parseInt(currentDishList.getSelectionModel().getSelectedItem().toString().split(":")[0])){
                dish.getIngredients().remove(j);
                dish.getAmount().remove(j);
                currentDishList.getItems().remove(j);
                break;
            }
        }
    }
//-------------------------------------------------FOOD LIST TAB END--------------------------------------------------//



//---------------------------------------------FOOD MANAGEMENT TAB START----------------------------------------------//
    public void searchFoodManage(){
        foodNameManagement.clear();
        foodPriceManagement.clear();
        foodPartsAmountManagement.clear();
        foodStatusManagement.setValue(null);
        foodNotesManagement.clear();

        addFoodButton.setDisable(false);
        updateFoodButton.setDisable(true);

        searchFood(foodManagementList, searchFoodNameManagement, searchFoodStatusManagement);
    }

        public void ClearFoodManagementFields() {
        addFoodButton.setDisable(false);
        updateFoodButton.setDisable(true);
            foodNameManagement.clear();
            foodPriceManagement.clear();
            foodPartsAmountManagement.clear();
            foodStatusManagement.setValue(null);
            foodNotesManagement.clear();
    }

    public void LoadFoodInfo(){

        addFoodButton.setDisable(true);
        updateFoodButton.setDisable(false);

        Food currentFood = getFoodById(foodManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentFood ==null){
            alertMsg("This food is unavailable.","It was removed.");
        }
        else {
            foodNameManagement.setText(currentFood.getName());
            foodPriceManagement.setText(String.valueOf(currentFood.getPrice()));
            foodPartsAmountManagement.setText(String.valueOf(currentFood.getPartsAmount()));
            foodStatusManagement.setValue(currentFood.getStatus());
            foodNotesManagement.setText(currentFood.getNotes());
        }
    }
    public void DeleteFood(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Permission Dialog");
        alert.setHeaderText("Are you sure that you want to remove food?");
        alert.setContentText("The current food, its amount in dishes and the food itself will be removed!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int currentFoodId=Integer.parseInt(foodManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]);
                Food currentFood=foodHibernateCtrl.getFoodById(currentFoodId);
                List<Dish> dishes = dishHibernateCtrl.getAllDishes();

                for(Dish tempDish : dishes){
                    if(tempDish.getIngredients().contains(currentFood)) {
                        while (tempDish.getIngredients().contains(currentFood)){
                            int index=tempDish.getIngredients().indexOf(currentFood);

                            System.out.println("Index " + index);

                            tempDish.getIngredients().remove(index);
                            tempDish.getAmount().remove(index);
                        }

                        currentFood.getInDishes().remove(currentFood.getInDishes().indexOf(tempDish));
                        dishHibernateCtrl.updateDish(tempDish);
                        if(tempDish.getIngredients().isEmpty()&&tempDish.getAmount().isEmpty()){
                            dishHibernateCtrl.removeDish(tempDish.getId());
                        }
                    }
                }
                foodHibernateCtrl.removeFood(currentFoodId);

                int i=0;
                for(Object item : foodManagementList.getItems())
                {
                    if(currentFoodId == Integer.parseInt(item.toString().split(":")[0])){
                        foodManagementList.getItems().remove(i);
                        break;
                    }
                    i++;
                }
                ClearFoodManagementFields();
            }
        });
    }

    public void CreateFood() {
        if(foodPriceManagement.getText().isEmpty()){
            alertMsg("Food price field is empty!","Enter price!");
        }

        else if(containsCharactersDouble(foodPriceManagement.getText())){
            alertMsg("Food price contains characters!","Remove all characters!");
        }

        else if(foodPartsAmountManagement.getText().isEmpty()){
            alertMsg("Food amount field is empty!","Enter amount/pieces!");
        }

        else if(containsCharactersInteger(foodPartsAmountManagement.getText())){
            alertMsg("Food amount field contains characters!","Remove all characters!");
        }

        else {
            Food currentFood = new Food(foodNameManagement.getText(),
                    Math.floor(100.0*Double.parseDouble(foodPriceManagement.getText()))/100.0,
                    Integer.parseInt(foodPartsAmountManagement.getText()),
                    foodNotesManagement.getText(),
                    foodStatusManagement.getSelectionModel().getSelectedItem()==null
                            ? null : Status.valueOf(foodStatusManagement.getSelectionModel().getSelectedItem().toString()
                   ));

            foodHibernateCtrl.createFood(currentFood);
            updateFoodButton.setDisable(true);
            searchFoodManage();
        }
    }

    public void UpdateFood() {
        Food currentFood = getFoodById(foodManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]);

        if(currentFood ==null){
            alertMsg("This book is unavailable.","It was removed.");
        }
        else if(foodPartsAmountManagement.getText().isEmpty()){
            alertMsg("Food amount field is empty!","Enter amount/pieces!");
        }
        else if(containsCharactersInteger(foodPartsAmountManagement.getText())){
            alertMsg("Food page number contains characters!","Remove all characters!");
        }
        else if(foodPriceManagement.getText().isEmpty()){
            alertMsg("Food price field is empty!","Enter price!");
        }
        else if(containsCharactersDouble(foodPriceManagement.getText())){
            alertMsg("Food price field contains characters!","Remove all characters!");
        }
        else {
            currentFood.setName(foodNameManagement.getText());
            currentFood.setPrice(Math.floor(100.0*Double.parseDouble(foodPriceManagement.getText()))/100.0);
            currentFood.setPartsAmount(Integer.parseInt(foodPartsAmountManagement.getText()));
            currentFood.setNotes(foodNotesManagement.getText());
            currentFood.setStatus(Status.valueOf(foodStatusManagement.getSelectionModel().getSelectedItem().toString()));

            foodHibernateCtrl.updateFood(currentFood);

            ClearFoodManagementFields();
            addFoodButton.setDisable(false);
            updateFoodButton.setDisable(true);
            searchFoodManage();
        }
    }
//---------------------------------------------FOOD MANAGEMENT TAB END------------------------------------------------//



//--------------------------------------------DISH MANAGEMENT TAB START-----------------------------------------------//
    public void searchDish(ListView dishList, TextField name, ComboBox status, ComboBox portionSize) {
        dishList.getItems().clear();

        Status tempStatus=status.getSelectionModel().getSelectedItem()==null
                ? null : Status.valueOf(status.getSelectionModel().getSelectedItem().toString());
        PortionSize tempPortionSize=portionSize.getSelectionModel().getSelectedItem()==null
                ? null : PortionSize.valueOf(portionSize.getSelectionModel().getSelectedItem().toString());

        List<Dish> filteredDishes = dishHibernateCtrl.getFilteredDishes(name.getText(),tempStatus, tempPortionSize);

        filteredDishes.forEach(dish -> dishList.getItems().add(
                dish.getId() +
                ":" + dish.getName() +
                " price: " + DishPrice(dish.getIngredients(),dish.getAmount())
        ));
    }

    private double DishPrice(List<Food> foods, List<Integer> amount){
        double price=0;

        Food currentFood=new Food();
        for(int i=0;i<foods.size();i++){
            currentFood=foods.get(i);
            price+=currentFood.getPrice()*amount.get(i)/currentFood.getPartsAmount();
        }
        return Math.round(100.00*price)/100.00;
    }

    public void searchDishManage(){
        dishFoodList.getItems().clear();
        dishNameManagement.clear();
        dishStatusManagement.setValue(null);
        dishPortionSizeManagement.setValue(null);
        foodInDishAmount.clear();
        updateDishButton.setDisable(true);

        foodInDishAmount.setDisable(true);
        changeAmountInDishButton.setDisable(true);

        searchDish(dishManagementList, searchDishName,searchDishStatus, searchDishPortionSize);
    }

    public void LoadDishInfo(){
        Dish currentDish = dishHibernateCtrl.getDishById(Integer.parseInt(dishManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        List<Food> currentFoods=currentDish.getIngredients();
        List<Integer> currentAmounts=currentDish.getAmount();

        dishNameManagement.setText(currentDish.getName());
        dishStatusManagement.setValue(currentDish.getStatus());
        dishPortionSizeManagement.setValue(currentDish.getPortionSize());

        dishFoodList.getItems().clear();
        for(int i=0;i<currentFoods.size();i++){
            Food tempFood=currentFoods.get(i);
            dishFoodList.getItems().add(tempFood.getId()+":"+tempFood.getName()+" amount: "+currentAmounts.get(i));
        }

        foodInDishAmount.setDisable(true);
        changeAmountInDishButton.setDisable(true);
        updateDishButton.setDisable(false);
    }

    public void DeleteDish(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Permission Dialog");
        alert.setHeaderText("Are you sure that you want to remove dish?");
        alert.setContentText("The current dish and its components info will be removed!");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("DELETING DISH STARTED");

                int currentDishId=Integer.parseInt(dishManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]);
                Dish currentDish=dishHibernateCtrl.getDishById(currentDishId);

                for(Food tempFood : currentDish.getIngredients()){
                    System.out.println("REMOVING FOOD FROM DISHES");
                    while (tempFood.getInDishes().contains(currentDish)) {
                        int index = tempFood.getInDishes().indexOf(currentDish);

                        System.out.println("Index " + index);

                        tempFood.getInDishes().remove(index);
                    }
                    //foodHibernateCtrl.updateFood(tempFood);
                }

                int size=currentDish.getIngredients().size();
                for(int i=0; i<size;i++){
                    System.out.println("CLEANING DISH");
                    currentDish.getIngredients().remove(0);
                    currentDish.getAmount().remove(0);
                }

                dishHibernateCtrl.updateDish(currentDish);
                dishHibernateCtrl.removeDish(currentDishId);

                /*for(Dish tempDish : dishes){
                    if(tempDish.getIngredients().contains(currentFood)) {
                        while (tempDish.getIngredients().contains(currentFood)){
                            int index=tempDish.getIngredients().indexOf(currentFood);

                            System.out.println("Index " + index);

                            tempDish.getIngredients().remove(index);
                            tempDish.getAmount().remove(index);
                        }

                        currentFood.getInDishes().remove(currentFood.getInDishes().indexOf(tempDish));
                        dishHibernateCtrl.updateDish(tempDish);
                        if(tempDish.getIngredients().isEmpty()&&tempDish.getAmount().isEmpty()){
                            dishHibernateCtrl.removeDish(tempDish.getId());
                        }
                    }
                }

                //foodHibernateCtrl.updateFood(currentFood);
                foodHibernateCtrl.removeFood(currentFoodId);*/

                int i=0;
                for(Object item : dishManagementList.getItems())
                {
                    if(currentDishId == Integer.parseInt(item.toString().split(":")[0])){
                        dishManagementList.getItems().remove(i);
                        break;
                    }
                    i++;
                }
                ClearDishManagementFields();
                dishFoodList.getItems().clear();
            }
        });


    }

    public void UpdateDish(){
        Dish currentDish=dishHibernateCtrl.getDishById(Integer.parseInt(dishManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        currentDish.setName(dishNameManagement.getText());
        currentDish.setStatus(dishStatusManagement.getSelectionModel().getSelectedItem()==null
                ? null : Status.valueOf(dishStatusManagement.getSelectionModel().getSelectedItem().toString()
        ));
        currentDish.setPortionSize(dishPortionSizeManagement.getSelectionModel().getSelectedItem()==null
                ? null : PortionSize.valueOf(dishPortionSizeManagement.getSelectionModel().getSelectedItem().toString()
        ));

        dishHibernateCtrl.updateDish(currentDish);
        searchDishManage();

        updateDishButton.setDisable(true);
    }

    public void ChangeAmountInDish(){
        Dish currentDish=dishHibernateCtrl.getDishById(
                Integer.parseInt(dishManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        List<Integer> currentAmounts=currentDish.getAmount();

        int index=0;
        int tempID=Integer.parseInt(dishFoodList.getSelectionModel().getSelectedItem().toString().split(":")[0]);
        for(Object item : dishFoodList.getItems())
        {
            if(tempID == Integer.parseInt(item.toString().split(":")[0])){
                break;
            }
            index++;
        }

        if (foodInDishAmount.getText().isEmpty()) {
            alertMsg("Field is empty!", "Enter amount/pieces!");
        }
        else if (containsCharactersInteger(foodInDishAmount.getText())) {
            alertMsg("Field contains characters!", "Remove all characters!");
        }
        else {
            currentAmounts.remove(index);
            currentAmounts.add(index, Integer.parseInt(foodInDishAmount.getText()));

            if(currentDish.getIngredients().size()==currentAmounts.size())
            {
                dishHibernateCtrl.updateDish(currentDish);
            }

            foodInDishAmount.setDisable(true);
            foodInDishAmount.clear();
            changeAmountInDishButton.setDisable(true);
            searchDishManage();
            dishFoodList.getItems().clear();
        }
    }

    public void EnableAmountChanging(){
        if(dishManagementList.getSelectionModel().getSelectedItem()!=null)
        {
            foodInDishAmount.setDisable(false);
            changeAmountInDishButton.setDisable(false);
        }
    }

    public void ClearDishManagementFields(){
        dishNameManagement.clear();
        dishStatusManagement.setValue(null);
        dishPortionSizeManagement.setValue(null);
    }

    public void AddDishToList(){
        Dish copiedDish=dishHibernateCtrl.getDishById(Integer.parseInt(dishManagementList.getSelectionModel().getSelectedItem().toString().split(":")[0]));

        if(copiedDish.getIngredients().size()==copiedDish.getAmount().size())
        {
            for(int i=0;i<copiedDish.getIngredients().size();i++){
                dish.getAmount().add(copiedDish.getAmount().get(i));
                dish.getIngredients().add(copiedDish.getIngredients().get(i));
                currentDishList.getItems().add(dish.getIngredients().get(i).getId() + ":" + dish.getIngredients().get(i).getName());
            }
        }

        else{
            alertMsg("Ingredients and their amounts numbers aren't the same","Fix this issue before adding dish to the list");
        }
        //currentDishList.getItems().add(tempFood.getId() + ":" + tempFood.getName());
    }
//--------------------------------------------DISH MANAGEMENT TAB END-------------------------------------------------//





}

