package com.example.testfx;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.example.testfx.DateBase.Animal;
import com.example.testfx.DateBase.DatabaseHandler;
import com.example.testfx.DateBase.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button loginSignInButton;

    @FXML
    private Button loginSignUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private TextField password_field;

    private static byte attempts = 0;       // Количество неудачных попыток входа
    private static Calendar date2 = null;   // Время первой неудачной попытки входа

    @FXML
    void initialize() {
        loginSignInButton.setOnAction(event -> {
            String loginText = login_field.getText().trim();
            String loginPassword = password_field.getText().trim();

            if ( !loginText.equals("") && !loginPassword.equals("") && attempts < 10) {
                loginUser(loginText, loginPassword);
            }
            else if (attempts >= 10) {
                    App.error_out("Too many invalid login attempts!"+remain_time());
            }
            else {
                attempts += 1;
                remember_the_time();
                App.error_out("Login or password is empty!"+remain_time());
            }
        });

        loginSignUpButton.setOnAction(event -> {
            loginSignUpButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("signUP.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
    }

    // Авторизация пользователя
    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;

        try {
            while (result.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Статус входа
        if (counter == 0) {         // Неудачный
            attempts += 1;
            remember_the_time();
            App.error_out("Wrong login or password!"+remain_time());
        }
        else {                      // Вход выполнен
            attempts = 0;

            Animal.owner = loginText;

            loginSignInButton.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("when_login.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }

    }

    // Запомнить время первой неудачной попытки входа
    private void remember_the_time() {
        if (date2 == null) {    // Начинаем отсчет
            date2 = Calendar.getInstance();
            date2.add(Calendar.MINUTE,60);
        }
    }

    // Счетик и вывод оставшегося времени
    private String remain_time() {
        Calendar date1 = Calendar.getInstance();
        int remain = date2.get(Calendar.HOUR_OF_DAY)*3600 + date2.get(Calendar.MINUTE)*60 + date2.get(Calendar.SECOND) - (date1.get(Calendar.HOUR_OF_DAY)*3600 + date1.get(Calendar.MINUTE)*60 + date1.get(Calendar.SECOND));
        if (remain <= 0) {      // Обнуление, когда пройдет время
            date2 = null;
            attempts = 0;
        }
        return " Remain "+remain/60+" mins "+remain % 60 +" secs";
    }
}
