package src.inventario.controller.marcaje;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Marcaje;

import java.io.IOException;
import java.util.List;

public class MarcajeController {

    @FXML
    private TableView<Marcaje> marcajeTable;

    @FXML
    private TableColumn<Marcaje, String> productoColumn;

    @FXML
    private TableColumn<Marcaje, String> aulaColumn;

    @FXML
    private TableColumn<Marcaje, String> tipoColumn;

    @FXML
    private void initialize() {
        productoColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getIdProducto().getDescripcion()));
        aulaColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getIdAula().getNumeracion()));
        tipoColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().getTipo().toString()));

        loadMarcajes();
    }

    private void loadMarcajes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Marcaje> marcajes = session.createQuery(
                    "select distinct m from Marcaje m left join fetch m.idProducto left join fetch m.idAula",
                    Marcaje.class
            ).list();
            marcajeTable.getItems().setAll(marcajes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCrear() {
        openForm("/src/inventario/marcaje/crear-marcaje-view.fxml", null);
    }

    @FXML
    private void handleModificar() {
        Marcaje selectedMarcaje = marcajeTable.getSelectionModel().getSelectedItem();
        if (selectedMarcaje != null) {
            openForm("/src/inventario/marcaje/modificar-marcaje-view.fxml", selectedMarcaje);
        } else {
            showAlert("Seleccione un marcaje", "Por favor, seleccione un marcaje para modificar.");
        }
    }

    @FXML
    private void handleEliminar() {
        Marcaje selectedMarcaje = marcajeTable.getSelectionModel().getSelectedItem();
        if (selectedMarcaje != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.remove(selectedMarcaje);
                transaction.commit();
                loadMarcajes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Seleccione un marcaje", "Por favor, seleccione un marcaje para eliminar.");
        }
    }

    private void openForm(String viewPath, Marcaje marcaje) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            if (viewPath.contains("crear-marcaje-view.fxml")) {
                CrearMarcajeController controller = loader.getController();
                controller.setStage(stage);
            } else if (viewPath.contains("modificar-marcaje-view.fxml")) {
                ModificarMarcajeController controller = loader.getController();
                controller.setMarcaje(marcaje);
                controller.setStage(stage);
            }

            stage.showAndWait();
            loadMarcajes();
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
