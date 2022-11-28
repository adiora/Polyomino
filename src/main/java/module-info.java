module com.adioracreations.polyominoes {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.adioracreations.polyominoes to javafx.fxml;
    exports com.adioracreations.polyominoes;
}