package com.example.testfx;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import com.example.testfx.DateBase.DatabaseHandler;
import com.example.testfx.DateBase.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerSignUp {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button checkNameButton;

    @FXML
    private TextField checkNameField;

    @FXML
    private Button signUP;

    @FXML
    private TextField signup_Login;

    @FXML
    private TextField signup_Password;

    @FXML
    void initialize() {
        signUP.setOnAction(event -> signUpNewUser());

        checkNameButton.setOnAction(event -> {
            String new_name = checkNameField.getText().trim();

            if (AvailabilityFreeName(new_name))
                    App.error_out("Nickname is free!");
        });
    }

    // Проверка на уникальность нового ника
    private boolean AvailabilityFreeName(String new_name) {
        if (new_name.equals("")) { App.error_out("Nickname field is empty!"); return false;}

        if (new_name.length() > 16) { App.error_out("Max nickname length 16!"); return false;}

        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin(new_name);
        ResultSet result = dbHandler.getUsername(user);

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

    // Регистрация нового пользователя. Занесение в БД, загрузка интерфейса авторизированного пользователя
    private void signUpNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        String signUpText = signup_Login.getText().trim();
        String signUpPassword = signup_Password.getText().trim();

        if (!signUpPassword.equals("") && signUpPassword.length()<=16) {
            if (AvailabilityFreeName(signUpText)) {
                User user = new User(signup_Login.getText(), signup_Password.getText());

                dbHandler.signUpUser(user);

                Stage ss = (Stage) signUP.getScene().getWindow();
                ss.close();
                Stage stage = new Stage();

                try {
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("when_login.fxml")));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (signUpText.length()<=16 || signUpPassword.length()<=16)
            App.error_out("Max password length 16!");
        else
            App.error_out("Password field is empty!");
    }
}
