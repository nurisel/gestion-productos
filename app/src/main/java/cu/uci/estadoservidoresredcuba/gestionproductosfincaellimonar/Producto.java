package cu.uci.estadoservidoresredcuba.gestionproductosfincaellimonar;

/**
 * Created by tatos on 30/01/18.
 */

public class Producto {
    private String id;
    private String nombre;
    private int disponibilidad;
    private float precio;

    public Producto(String id, String nombre, int disponibilidad, float precio) {
        this.id = id;
        this.nombre = nombre;
        this.disponibilidad = disponibilidad;
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
