module org.example.demo_ex {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.demo_ex to javafx.fxml;
    opens org.example.demo_ex.controller to javafx.fxml;
    opens org.example.demo_ex.model to javafx.fxml;
    exports org.example.demo_ex;
    exports org.example.demo_ex.controller;
    exports org.example.demo_ex.model;
}
