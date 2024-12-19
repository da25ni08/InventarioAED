package src.inventario.model;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "productos", schema = "inventario")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProducto", nullable = false)
    private Integer id;

    @Column(name = "Descripcion", nullable = false)
    private String descripcion;

    @Column(name = "EAN13", nullable = false)
    private Integer ean13;

    @Column(name = "keyRFID", nullable = false, length = 10)
    private String keyRFID;

    @OneToMany(mappedBy = "idProducto")
    private Set<Marcaje> marcajes = new LinkedHashSet<>();

    public Producto() {
    }

    public Producto(String descripcion, Integer ean13, String keyRFID) {
        this.descripcion = descripcion;
        this.ean13 = ean13;
        this.keyRFID = keyRFID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEan13() {
        return ean13;
    }

    public void setEan13(Integer ean13) {
        this.ean13 = ean13;
    }

    public String getKeyRFID() {
        return keyRFID;
    }

    public void setKeyRFID(String keyRFID) {
        this.keyRFID = keyRFID;
    }

    public Set<Marcaje> getMarcajes() {
        return marcajes;
    }

    public void setMarcajes(Set<Marcaje> marcajes) {
        this.marcajes = marcajes;
    }
}
