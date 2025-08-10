module com.store_management.store_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mysql.connector.j;

    exports com.store_management.store_management;
    opens com.store_management.store_management.controller to javafx.fxml, javafx.base;

    opens com.store_management.store_management to javafx.fxml;
    exports com.store_management.store_management.entity;
    opens com.store_management.store_management.entity to javafx.base, javafx.fxml;

}