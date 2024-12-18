package src.inventario;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.hibernate.Session;

import java.awt.event.ActionEvent;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private void onCreate(ActionEvent event) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        welcomeText.setText("Hello World!");
        session.getTransaction().commit();
        session.close();

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}