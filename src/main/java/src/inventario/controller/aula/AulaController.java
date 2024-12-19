package src.inventario.controller.aula;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Aula;

import java.io.IOException;
import java.util.List;

public class AulaController {

    @FXML
    private TableView<Aula> aulaTable;

    @FXML
    private TableColumn<Aula, String> numeracionColumn;

    @FXML
    private TableColumn<Aula, String> descripcionColumn;

    @FXML
    private TableColumn<Aula, String> ipColumn;

    @FXML
    private void initialize() {
        numeracionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNumeracion()));
        descripcionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescripcion()));
        ipColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIp()));

        loadAulas();
    }

    private void loadAulas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Aula> aulas = session.createQuery("from Aula", Aula.class).list();
            aulaTable.getItems().setAll(aulas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCrear() {
        openCrearAulaForm();
    }

    @FXML
    private void handleModificar() {
        Aula selectedAula = aulaTable.getSelectionModel().getSelectedItem();
        if (selectedAula != null) {
            openModificarAulaForm(selectedAula);
        } else {
            showAlert("Seleccione un aula", "Por favor, seleccione un aula para modificar.");
        }
    }

    @FXML
    private void handleEliminar() {
        Aula selectedAula = aulaTable.getSelectionModel().getSelectedItem();
        if (selectedAula != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(selectedAula);
                transaction.commit();
                loadAulas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Seleccione un aula", "Por favor, seleccione un aula para eliminar.");
        }
    }

    private void openCrearAulaForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/inventario/aula/crear-aula-view.fxml"));
            Stage formStage = new Stage();
            formStage.setScene(new Scene(loader.load()));
            CrearAulaController controller = loader.getController();
            controller.setStage(formStage);
            formStage.showAndWait();
            loadAulas();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openModificarAulaForm(Aula aula) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/inventario/aula/modificar-aula-view.fxml"));
            Stage formStage = new Stage();
            formStage.setScene(new Scene(loader.load()));
            ModificarAulaController controller = loader.getController();
            controller.setAula(aula);
            controller.setStage(formStage);
            formStage.showAndWait();
            loadAulas();
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