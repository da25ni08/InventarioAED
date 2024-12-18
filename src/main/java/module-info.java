module src.inventario {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.desktop;
    requires java.naming;

    opens src.inventario to javafx.fxml;
    exports src.inventario;
    exports src.inventario.model;
    opens src.inventario.controllers to javafx.fxml;
    opens src.inventario.model to org.hibernate.orm.core;
    exports src.inventario.controllers;
}