module com.main.invento {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.main.invento to javafx.fxml;
    exports com.main.invento;
}