package src.inventario;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory; //Creamos un objeto de tipo SessionFactory
    private static Configuration configuration = new Configuration().configure(); //Creamos un objeto de tipo Configuration
    //Nos conectamos a la base de datos (Nos devuelve la sesion)
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = configuration.buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return sessionFactory;
    }
    //Cerramos la sesion
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
