package empresa.android.aplicacionmobil_el_nogal.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Producto implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("material")
    private String material;

    @SerializedName("precioVenta")
    private double precioVenta;

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMaterial() {
        return material;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }
}
