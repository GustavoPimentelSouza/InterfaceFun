module com.example.interfacefun {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.interfacefun.controller to javafx.fxml;
    opens com.example.interfacefun.model to javafx.base;

    exports com.example.interfacefun;
}
