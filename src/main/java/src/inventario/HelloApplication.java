package src.inventario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kordamp.bootstrapfx.BootstrapFX;
import src.inventario.model.Aula;
import src.inventario.model.Marcaje;
import src.inventario.model.Producto;

import java.io.IOException;
import java.time.Instant;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        inicializarDatosDePrueba();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("marcaje/marcaje-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setTitle("Hello!");

        stage.setScene(scene);
        stage.show();
    }
    public static void inicializarDatosDePrueba() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Crear Aulas
            Aula aula1 = new Aula();
            aula1.setNumeracion("1.1.1");
            aula1.setDescripcion("Aula principal");
            aula1.setIp("192.168.1.1");

            Aula aula2 = new Aula();
            aula2.setNumeracion("2.1.2");
            aula2.setDescripcion("Laboratorio");
            aula2.setIp("192.168.1.2");

            session.persist(aula1);
            session.persist(aula2);

            // Crear Productos
            Producto producto1 = new Producto();
            producto1.setDescripcion("Ordenador portátil");
            producto1.setEan13(1234567);
            producto1.setKeyRFID("RFID123");

            Producto producto2 = new Producto();
            producto2.setDescripcion("Proyector");
            producto2.setEan13(987654321);
            producto2.setKeyRFID("RFID456");

            session.persist(producto1);
            session.persist(producto2);

            // Crear Marcajes
            Marcaje marcaje1 = new Marcaje();
            marcaje1.setIdProducto(producto1);
            marcaje1.setIdAula(aula1);
            marcaje1.setTipo(1);
            marcaje1.setTimeStamp(Instant.now());

            Marcaje marcaje2 = new Marcaje();
            marcaje2.setIdProducto(producto2);
            marcaje2.setIdAula(aula2);
            marcaje2.setTipo(2);
            marcaje2.setTimeStamp(Instant.now());

            session.persist(marcaje1);
            session.persist(marcaje2);

            transaction.commit();

            System.out.println("Datos de prueba inicializados correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}