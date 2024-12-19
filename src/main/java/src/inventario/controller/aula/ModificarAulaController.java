package src.inventario.controller.aula;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Aula;

public class ModificarAulaController {

    @FXML
    private Node formularioAula;

    private TextField numeracionField;
    private TextField descripcionField;
    private TextField ipField;

    private Stage stage;
    private Aula aula;

    public void setAula(Aula aula) {
        this.aula = aula;
        if (aula != null) {
            numeracionField.setText(aula.getNumeracion());
            descripcionField.setText(aula.getDescripcion());
            ipField.setText(aula.getIp());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        if (formularioAula != null) {
            numeracionField = (TextField) formularioAula.lookup("#numeracionField");
            descripcionField = (TextField) formularioAula.lookup("#descripcionField");
            ipField = (TextField) formularioAula.lookup("#ipField");
        }
    }

    @FXML
    private void handleGuardar() {
        if (numeracionField == null || descripcionField == null || ipField == null) {
            System.out.println("Campos no inicializados correctamente.");
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            aula.setNumeracion(numeracionField.getText());
            aula.setDescripcion(descripcionField.getText());
            aula.setIp(ipField.getText());
            session.update(aula);
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
