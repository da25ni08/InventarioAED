package src.inventario.controller.producto;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Producto;

public class CrearProductoController {

    @FXML
    private Node formularioProducto;

    private TextField descripcionField;
    private TextField eanField;
    private TextField rfidField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        if (formularioProducto != null) {
            descripcionField = (TextField) formularioProducto.lookup("#descripcionField");
            eanField = (TextField) formularioProducto.lookup("#eanField");
            rfidField = (TextField) formularioProducto.lookup("#rfidField");
        }
    }

    @FXML
    private void handleGuardar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Producto producto = new Producto(
                    descripcionField.getText(),
                    Integer.parseInt(eanField.getText()),
                    rfidField.getText()
            );
            session.save(producto);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stage.close();
        }
    }

    @FXML
    private void handleCancelar() {
        stage.close();
    }
}

