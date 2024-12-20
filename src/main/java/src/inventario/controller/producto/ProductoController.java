package src.inventario.controller.producto;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Producto;

import java.io.IOException;
import java.util.List;

public class ProductoController {

    @FXML
    private TableView<Producto> productoTable;

    @FXML
    private TableColumn<Producto, String> descripcionColumn;

    @FXML
    private TableColumn<Producto, String> eanColumn;

    @FXML
    private TableColumn<Producto, String> rfidColumn;

    @FXML
    private void initialize() {
        descripcionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescripcion()));
        eanColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEan13().toString()));
        rfidColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getKeyRFID()));

        loadProductos();
    }

    private void loadProductos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Producto> productos = session.createQuery(
                    "select distinct p from Producto p left join fetch p.categorias", Producto.class
            ).list();
            productoTable.getItems().setAll(productos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCrear() {
        openCrearForm();
    }

    @FXML
    private void handleModificar() {
        Producto selectedProducto = productoTable.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            openModificarForm(selectedProducto);
        } else {
            showAlert("Seleccione un producto", "Por favor, seleccione un producto para modificar.");
        }
    }

    @FXML
    private void handleEliminar() {
        Producto selectedProducto = productoTable.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(selectedProducto);
                transaction.commit();
                loadProductos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Seleccione un producto", "Por favor, seleccione un producto para eliminar.");
        }
    }

    private void openCrearForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/inventario/producto/crear-producto-view.fxml"));
            Stage formStage = new Stage();
            formStage.setScene(new Scene(loader.load()));
            CrearProductoController controller = loader.getController();
            controller.setStage(formStage);
            formStage.showAndWait();
            loadProductos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openModificarForm(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/inventario/producto/modificar-producto-view.fxml"));
            Stage formStage = new Stage();
            formStage.setScene(new Scene(loader.load()));
            ModificarProductoController controller = loader.getController();
            controller.setProducto(producto);
            controller.setStage(formStage);
            formStage.showAndWait();
            loadProductos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

