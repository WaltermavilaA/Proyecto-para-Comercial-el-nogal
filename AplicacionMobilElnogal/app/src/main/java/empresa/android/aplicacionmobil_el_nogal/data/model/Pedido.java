package empresa.android.aplicacionmobil_el_nogal.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("numeroPedido")
    private String numeroPedido;

    @SerializedName("fechaPedido")
    private String fechaPedido;

    @SerializedName("estado")
    private String estado;

    @SerializedName("subtotal")
    private double subtotal;

    @SerializedName("envio")
    private double envio;

    @SerializedName("total")
    private double total;

    @SerializedName("metodoPago")
    private String metodoPago;

    @SerializedName("notas")
    private String notas;

    @SerializedName("codigoVerificacion")
    private String codigoVerificacion;

    @SerializedName("direccion")
    private Direccion direccion;

    @SerializedName("tarjeta")
    private Tarjeta tarjeta;

    @SerializedName("detalles")
    private List<DetallePedido> detalles;

    @SerializedName("usuario")
    private Usuario usuario;

    public long getId() {
        return id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getEnvio() {
        return envio;
    }

    public double getTotal() {
        return total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public String getNotas() {
        return notas;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
