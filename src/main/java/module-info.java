module com.example.testfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    //requires org.controlsfx.controls;
    //requires com.dlsc.formsfx;
    //requires org.kordamp.bootstrapfx.core;

    opens com.example.testfx to javafx.fxml;
    exports com.example.testfx;
    exports com.example.testfx.DateBase;
    opens com.example.testfx.DateBase to javafx.fxml;
}