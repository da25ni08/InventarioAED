package src.inventario.controller.producto;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import src.inventario.HibernateUtil;
import src.inventario.model.Categoria;
import src.inventario.model.Producto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrearProductoController {

    @FXML
    private Node formularioProducto;

    @FXML
    private TextField descripcionField;

    @FXML
    private TextField eanField;

    @FXML
    private TextField rfidField;

    @FXML
    private ListView<String> categoriaListView;

    private List<Categoria> categoriasDisponibles;

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
            categoriaListView = (ListView<String>) formularioProducto.lookup("#categoriaListView");
        }
        categoriaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadCategorias();
    }

    private void loadCategorias() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            categoriasDisponibles = session.createQuery("from Categoria", Categoria.class).list();
            List<String> nombresCategorias = categoriasDisponibles.stream()
                    .map(Categoria::getNombre)
                    .collect(Collectors.toList());
            categoriaListView.setItems(FXCollections.observableArrayList(nombresCategorias));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGuardar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Obteniendo las categorías seleccionadas y asegurando que estén gestionadas por la sesión
            Set<Categoria> categoriasSeleccionadas = categoriaListView.getSelectionModel().getSelectedItems().stream()
                    .map(nombre -> categoriasDisponibles.stream()
                            .filter(categoria -> categoria.getNombre().equals(nombre))
                            .findFirst()
                            .orElse(null))
                    .filter(categoria -> categoria != null)
                    .map(categoria -> session.find(Categoria.class, categoria.getId())) // Asociar a la sesión
                    .collect(Collectors.toSet());

            // Crear y configurar el producto
            Producto producto = new Producto();
            producto.setDescripcion(descripcionField.getText());
            producto.setEan13(Integer.parseInt(eanField.getText()));
            producto.setKeyRFID(rfidField.getText());
            producto.setCategorias(categoriasSeleccionadas);

            // Guardar el producto
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
