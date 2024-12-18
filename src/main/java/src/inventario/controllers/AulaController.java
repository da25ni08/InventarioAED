package src.inventario.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Aula;

import java.util.List;

public class AulaController {

    @FXML
    private TextField numeracionField;

    @FXML
    private TextField descripcionField;

    @FXML
    private TextField ipField;

    @FXML
    private TableView<Aula> aulaTable;

    @FXML
    private TableColumn<Aula, String> numeracionColumn;

    @FXML
    private TableColumn<Aula, String> descripcionColumn;

    @FXML
    private TableColumn<Aula, String> ipColumn;

    private ObservableList<Aula> aulaList;

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
            aulaList = FXCollections.observableArrayList(aulas);
            aulaTable.setItems(aulaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddAula() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Aula aula = new Aula(numeracionField.getText(), descripcionField.getText(), ipField.getText());
            session.save(aula);
            transaction.commit();
            loadAulas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteAula() {
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
        }
    }

    @FXML
    private void handleUpdateAula() {
        Aula selectedAula = aulaTable.getSelectionModel().getSelectedItem();
        if (selectedAula != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                selectedAula.setNumeracion(numeracionField.getText());
                selectedAula.setDescripcion(descripcionField.getText());
                selectedAula.setIp(ipField.getText());
                session.update(selectedAula);
                transaction.commit();
                loadAulas();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
