package com.example.testfx;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.example.testfx.DateBase.Animal;
import com.example.testfx.DateBase.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ControllerLoginned {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button createAnimalButton;

    @FXML
    private TextField dateBirthIDout;

    @FXML
    private TextField dateOfBirth_field;

    @FXML
    private TextField enter_ID;

    @FXML
    private TextField genderIDout;

    @FXML
    private TextField gender_field;

    @FXML
    private Button getListButton;

    @FXML
    private Button getInfoAnimal;

    @FXML
    private TextField kindIDout;

    @FXML
    private TextField kind_field;

    @FXML
    private ListView<String> list_animals;

    @FXML
    private TextField nicknameIDout;

    @FXML
    private TextField nickname_field;

    @FXML
    private Button deleteAnimalButton;

    @FXML
    private Button saveEdittedAnimalButton;

    @FXML
    void initialize() {
        createAnimalButton.setOnAction(event -> {

            String nickname = nickname_field.getText().trim();
            String kind = kind_field.getText().trim();
            String gender = gender_field.getText().trim();
            String dateOfBirth = dateOfBirth_field.getText().trim();

            if (input_validation(nickname,kind,gender,dateOfBirth) && freeNameAnimal(nickname)) {
                Animal animal = new Animal(nickname, kind, gender, Date.valueOf(dateOfBirth));
                createAnimal(animal);
                getListButton.fire();
            }
        });

        getInfoAnimal.setOnAction(event -> {
            String ID = enter_ID.getText().trim();

            if (ID_validation(ID)) {
                int id = Integer.parseInt(ID);
                DatabaseHandler db = new DatabaseHandler();
                ArrayList<String> animal_info = new ArrayList<>(db.getAnimalInfo(id));
                kindIDout.clear();
                dateBirthIDout.clear();
                genderIDout.clear();
                nicknameIDout.clear();

                kindIDout.insertText(0, animal_info.get(1));
                dateBirthIDout.insertText(0, animal_info.get(3));
                genderIDout.insertText(0, animal_info.get(2));
                nicknameIDout.insertText(0, animal_info.get(0));
            }

        });

        getListButton.setOnAction(event -> {
            DatabaseHandler db = new DatabaseHandler();
            int id = db.findCountAnimals();

            list_animals.getItems().clear();
            if (id != 0)
                for (int i = 1; i < id+1; i++) {
                    list_animals.getItems().add(""+i);
                }
            else
                App.error_out("You don't have animals!");
        });

        deleteAnimalButton.setOnAction(event -> {
            DatabaseHandler db = new DatabaseHandler();
            String ID = enter_ID.getText().trim();
            int animals = db.findCountAnimals();

            if (ID_validation(ID)) {
                int id = Integer.parseInt(ID);
                db.deleteAnimal(id);
                for (int i = id; i < animals; i++) {
                    db.update_id(i, i+1);
                }
                getListButton.fire();
                App.message_out("Animal with ID "+id+" removed");
            }
        });

        saveEdittedAnimalButton.setOnAction(event -> {
            String ID = enter_ID.getText().trim();

            String nickname = nicknameIDout.getText().trim();
            String kind = kindIDout.getText().trim();
            String gender = genderIDout.getText().trim();
            String dateOfBirth = dateBirthIDout.getText().trim();

            if (ID_validation(ID) && input_validation(nickname, kind, gender, dateOfBirth) && freeNameAnimal(nickname)) {
                DatabaseHandler db = new DatabaseHandler();
                Animal animal = new Animal(nickname, kind, gender, Date.valueOf(dateOfBirth));
                db.updateEditAnimal(Integer.parseInt(ID), animal);
                App.message_out("Successfully updated!");
            }
        });

    }

    private void createAnimal(Animal animal) {
        DatabaseHandler db = new DatabaseHandler();
        Animal.id_own = db.findCountAnimals() + 1;
        db.NewAnimal(animal);
        App.message_out("Success!");
    }

    // Проверка введенных данных животного (Добавление нового, редактирование)
    private boolean input_validation(String nickname, String kind, String gender, String dateOfBirth) {
        final String animals;
        try {
            animals = readUsingFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String genders = " Female Male ";

        if (nickname.equals("") || kind.equals("") || gender.equals("") || dateOfBirth.equals("")) {
            App.error_out("Fields must not be empty!");
            return false;
        }

        if (!animals.contains(" "+kind+" ")) { App.error_out("Use Dog, Cat, Duck etc.!"); return false; }

        if (!dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
            App.error_out("Use yyyy-mm-dd format!");
            return false;
        }
        int m = Integer.parseInt(dateOfBirth.substring(5,7));
        int d = Integer.parseInt(dateOfBirth.substring(8));
        if (!(m > 0 && m <= 12)) {
            App.error_out("Invalid month value!");
            return false;
        }
        if (!(d > 0 && d <= 31)) {
            App.error_out("Invalid day value!");
            return false;
        }

        if (!genders.contains(" "+gender+" ")) { App.error_out("Use Female or Male!"); return false; }

        return true;
    }

    // Проверка ввода ID животного (получение информации, удаление и редактирование)
    private boolean ID_validation(String ID) {
        if (ID.equals("")) {
            App.error_out("Field ID must not be empty!");
            return false;
        }

        if (!ID.matches("\\d+")) { App.error_out("ID is the NUMBER of the animal!"); return false; }

        int animals = new DatabaseHandler().findCountAnimals();
        int id = Integer.parseInt(ID);

        if (id > animals) { App.error_out("You don't have such an animal!"); return false; }

        if (id <= 0) { App.error_out("ID cannot be less than 1!"); return false; }

        return true;
    }

    // Проверка на уникальность имени животного
    private boolean freeNameAnimal(String new_name) {
        DatabaseHandler db = new DatabaseHandler();
        Animal animal = new Animal();
        animal.setNickname(new_name);
        ResultSet result = db.getNameAnimal(animal);

        int counter = 0;
        try {
            while (result.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (counter != 0)
            App.error_out("Nickname is busy :(");

        return counter == 0;
    }

    // Считывание файла-справочника с видами животных
    private static String readUsingFiles() throws IOException {
        String fileName = "D:\\Other\\testfx\\src\\main\\resources\\com\\example\\testfx\\Animals.json";

        StringBuilder animals = new StringBuilder(" ");
        for (String c: new String(Files.readAllBytes(Paths.get(fileName))).split(", ")) {
            animals.append(c).append(" ");
        }
        return animals.toString();
    }
}