package com.adioracreations.polyominoes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(new AnchorPane());

        stage.setTitle("Polyominoes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Polyomino pentomino = new Polyomino(5);
        pentomino.generate();
        System.out.println(pentomino);

        launch();
    }
}