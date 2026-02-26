package empresa.android.aplicacionmobil_el_nogal.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetallePedido implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("cantidad")
    private int cantidad;

    @SerializedName("precioUnitario")
    private double precioUnitario;

    @SerializedName("subtotal")
    private double subtotal;

    @SerializedName("producto")
    private Producto producto;

    public long getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Producto getProducto() {
        return producto;
    }
}
