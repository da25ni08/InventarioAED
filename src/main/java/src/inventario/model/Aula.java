package src.inventario.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "aula", schema = "inventario")
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdAula", nullable = false)
    private Integer id;

    @Column(name = "Numeracion", nullable = false, length = 5)
    private String numeracion;

    @Column(name = "Descripcion", nullable = false)
    private String descripcion;

    @Column(name = "IP", nullable = false, length = 15)
    private String ip;

    @OneToMany(mappedBy = "idAula")
    private Set<Marcaje> marcajes = new LinkedHashSet<>();

    // Constructor sin argumentos (obligatorio para Hibernate)
    public Aula() {
    }

    // Constructor con parámetros
    public Aula(String numeracion, String descripcion, String ip) {
        this.numeracion = numeracion;
        this.descripcion = descripcion;
        this.ip = ip;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Set<Marcaje> getMarcajes() {
        return marcajes;
    }

    public void setMarcajes(Set<Marcaje> marcajes) {
        this.marcajes = marcajes;
    }
}
