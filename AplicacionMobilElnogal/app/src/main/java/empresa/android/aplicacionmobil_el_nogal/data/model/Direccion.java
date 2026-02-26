package empresa.android.aplicacionmobil_el_nogal.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Direccion implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("nombreDireccion")
    private String nombreDireccion;

    @SerializedName("nombreReceptor")
    private String nombreReceptor;

    @SerializedName("apellidosReceptor")
    private String apellidosReceptor;

    @SerializedName("direccion")
    private String direccion;

    @SerializedName("numero")
    private String numero;

    @SerializedName("distrito")
    private String distrito;

    @SerializedName("provincia")
    private String provincia;

    @SerializedName("departamento")
    private String departamento;

    @SerializedName("dptoOficinaCasa")
    private String dptoOficinaCasa;

    @SerializedName("telefono")
    private String telefono;

    public long getId() {
        return id;
    }

    public String getDireccionCompleta() {
        StringBuilder resultado = new StringBuilder();

        StringBuilder lineaPrincipal = new StringBuilder();
        if (direccion != null && !direccion.trim().isEmpty()) {
            lineaPrincipal.append(direccion.trim());
        }
        if (numero != null && !numero.trim().isEmpty()) {
            if (lineaPrincipal.length() > 0) {
                lineaPrincipal.append(" ");
            }
            lineaPrincipal.append(numero.trim());
        }
        if (lineaPrincipal.length() > 0) {
            resultado.append(lineaPrincipal);
        }

        StringBuilder lineaCiudad = new StringBuilder();
        if (distrito != null && !distrito.trim().isEmpty()) {
            lineaCiudad.append(distrito.trim());
        }
        if (provincia != null && !provincia.trim().isEmpty()) {
            if (lineaCiudad.length() > 0) {
                lineaCiudad.append(", ");
            }
            lineaCiudad.append(provincia.trim());
        }

        if (lineaCiudad.length() == 0 && departamento != null && !departamento.trim().isEmpty()) {
            lineaCiudad.append(departamento.trim());
        }

        if (lineaCiudad.length() > 0) {
            if (resultado.length() > 0) {
                resultado.append("\n");
            }
            resultado.append(lineaCiudad);
        }

        return resultado.toString();
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public String getApellidosReceptor() {
        return apellidosReceptor;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNumero() {
        return numero;
    }

    public String getDistrito() {
        return distrito;
    }

    public String getDptoOficinaCasa() {
        return dptoOficinaCasa;
    }

    public String getTelefono() {
        return telefono;
    }
}
