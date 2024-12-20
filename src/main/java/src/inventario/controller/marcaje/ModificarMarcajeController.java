package src.inventario.controller.marcaje;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Aula;
import src.inventario.model.Marcaje;
import src.inventario.model.Producto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ModificarMarcajeController {

    @FXML
    private Node formularioMarcaje;

    @FXML
    private ComboBox<String> productoComboBox;

    @FXML
    private ComboBox<String> aulaComboBox;

    @FXML
    private Spinner<Integer> horaSpinner;

    @FXML
    private Spinner<Integer> minutoSpinner;

    @FXML
    private DatePicker fechaPicker;

    @FXML
    private TextField tipoField;

    private Stage stage;
    private Marcaje marcaje;
    private List<Producto> productosDisponibles;
    private List<Aula> aulasDisponibles;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMarcaje(Marcaje marcaje) {
        this.marcaje = marcaje;
        loadProductos();
        loadAulas();
        populateFields();
    }

    @FXML
    private void initialize() {
        if (formularioMarcaje != null) {
            productoComboBox = (ComboBox<String>) formularioMarcaje.lookup("#productoComboBox");
            aulaComboBox = (ComboBox<String>) formularioMarcaje.lookup("#aulaComboBox");
            tipoField = (TextField) formularioMarcaje.lookup("#tipoField");
            fechaPicker = (DatePicker) formularioMarcaje.lookup("#fechaPicker");
            horaSpinner = (Spinner<Integer>) formularioMarcaje.lookup("#horaSpinner");
            minutoSpinner = (Spinner<Integer>) formularioMarcaje.lookup("#minutoSpinner");
        }

        if (productoComboBox == null || aulaComboBox == null || tipoField == null || fechaPicker == null || horaSpinner == null || minutoSpinner == null) {
            throw new IllegalStateException("Los elementos no se inicializaron correctamente.");
        }

        horaSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        minutoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }

    private void loadProductos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            productosDisponibles = session.createQuery("from Producto", Producto.class).list();
            productoComboBox.setItems(FXCollections.observableArrayList(
                    productosDisponibles.stream().map(Producto::getDescripcion).collect(Collectors.toList())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAulas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            aulasDisponibles = session.createQuery("from Aula", Aula.class).list();
            aulaComboBox.setItems(FXCollections.observableArrayList(
                    aulasDisponibles.stream().map(Aula::getNumeracion).collect(Collectors.toList())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateFields() {
        if (marcaje != null) {
            productoComboBox.setValue(marcaje.getIdProducto().getDescripcion());
            aulaComboBox.setValue(marcaje.getIdAula().getNumeracion());
            tipoField.setText(String.valueOf(marcaje.getTipo()));
            LocalDateTime fechaHora = LocalDateTime.ofInstant(marcaje.getTimeStamp(), ZoneId.systemDefault());
            fechaPicker.setValue(fechaHora.toLocalDate());
            horaSpinner.getValueFactory().setValue(fechaHora.getHour());
            minutoSpinner.getValueFactory().setValue(fechaHora.getMinute());
        }
    }

    @FXML
    private void handleGuardar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Producto productoSeleccionado = productosDisponibles.stream()
                    .filter(producto -> producto.getDescripcion().equals(productoComboBox.getValue()))
                    .findFirst()
                    .orElse(null);

            Aula aulaSeleccionada = aulasDisponibles.stream()
                    .filter(aula -> aula.getNumeracion().equals(aulaComboBox.getValue()))
                    .findFirst()
                    .orElse(null);

            LocalDate fecha = fechaPicker.getValue();
            LocalTime hora = LocalTime.of(horaSpinner.getValue(), minutoSpinner.getValue());
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            marcaje.setIdProducto(productoSeleccionado);
            marcaje.setIdAula(aulaSeleccionada);
            marcaje.setTipo(Integer.parseInt(tipoField.getText()));
            marcaje.setTimeStamp(fechaHora.atZone(ZoneId.systemDefault()).toInstant());

            session.merge(marcaje);
            transaction.commit();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        stage.close();
    }
}
