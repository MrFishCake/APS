package com.example.testfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setResizable(false);
        stage.setTitle("TestApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            launch();
            System.setErr(new PrintStream(new FileOutputStream(System.getProperty("user.home")+"/error.log")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void message_out(String title, String type) throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        Pane pane = new Pane();

        Text error_message = new Text(title);
        error_message.setX(10); error_message.setY(60);

        Button btn = new Button("Ok!");
        btn.setAlignment(Pos.CENTER);
        btn.setLayoutX(130); btn.setLayoutY(150);
        btn.setOnAction(event -> stage.close());

        pane.getChildren().add(btn);
        pane.getChildren().add(error_message);
        Scene scene = new Scene(pane, 320, 200);
        stage.setTitle(type);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public static void error_out(String str) {
        try {
            App.message_out(str, "Error!");
        } catch (IOException e) { System.err.println("App.error_message ERROR! \n"+str); }
    }
    public static void message_out(String str) {
        try {
            App.message_out(str, "Notice!");
        } catch (IOException e) { System.err.println("App.notice_message ERROR! \n"+str); }
    }
}