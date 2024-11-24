module com.main.invento {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    opens com.main.invento to javafx.fxml;
    opens com.main.invento.controllers to javafx.fxml;
    exports com.main.invento;
}