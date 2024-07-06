module org.yoosha.core {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires spring.beans;
    requires org.xerial.sqlitejdbc;
    requires spring.context;
    requires spring.core;
    requires java.json;

    opens org.yoosha.core to javafx.fxml;
    exports org.yoosha.core;

}