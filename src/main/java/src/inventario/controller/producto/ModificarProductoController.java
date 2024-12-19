package src.inventario.controller.producto;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Producto;

public class ModificarProductoController {

    @FXML
    private Node formularioProducto;

    private TextField descripcionField;
    private TextField eanField;
    private TextField rfidField;

    private Stage stage;
    private Producto producto;

    @FXML
    private void initialize() {
        if (formularioProducto != null) {
            descripcionField = (TextField) formularioProducto.lookup("#descripcionField");
            eanField = (TextField) formularioProducto.lookup("#eanField");
            rfidField = (TextField) formularioProducto.lookup("#rfidField");
        }
    }


    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            descripcionField.setText(producto.getDescripcion());
            eanField.setText(String.valueOf(producto.getEan13()));
            rfidField.setText(producto.getKeyRFID());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleGuardar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            producto.setDescripcion(descripcionField.getText());
            producto.setEan13(Integer.parseInt(eanField.getText()));
            producto.setKeyRFID(rfidField.getText());
            session.update(producto);
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

