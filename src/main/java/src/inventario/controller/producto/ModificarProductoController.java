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

public class ModificarProductoController {

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
    private Producto producto;

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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        if (producto != null) {
            descripcionField.setText(producto.getDescripcion());
            eanField.setText(String.valueOf(producto.getEan13()));
            rfidField.setText(producto.getKeyRFID());

            Set<String> nombresCategorias = producto.getCategorias().stream()
                    .map(Categoria::getNombre)
                    .collect(Collectors.toSet());

            categoriaListView.getSelectionModel().clearSelection();
            for (String categoria : categoriaListView.getItems()) {
                if (nombresCategorias.contains(categoria)) {
                    categoriaListView.getSelectionModel().select(categoria);
                }
            }
        }
    }

    @FXML
    private void handleGuardar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            List<String> categoriasSeleccionadasRaw = categoriaListView.getSelectionModel().getSelectedItems();
            Set<Categoria> categoriasSeleccionadas = new HashSet<>();

            for (String nombre : categoriasSeleccionadasRaw) {
                categoriasDisponibles.stream()
                        .filter(categoria -> categoria.getNombre().equals(nombre))
                        .findFirst()
                        .ifPresent(categoria -> {
                            // Utilizamos session.find para obtener la entidad gestionada por Hibernate
                            Categoria managedCategoria = session.find(Categoria.class, categoria.getId());
                            if (managedCategoria != null) {
                                categoriasSeleccionadas.add(managedCategoria);
                            }
                        });
            }

            producto.setDescripcion(descripcionField.getText());
            producto.setEan13(Integer.parseInt(eanField.getText()));
            producto.setKeyRFID(rfidField.getText());
            producto.setCategorias(categoriasSeleccionadas);

            session.merge(producto);
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
