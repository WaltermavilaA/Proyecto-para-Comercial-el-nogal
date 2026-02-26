package empresa.android.aplicacionmobil_el_nogal.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tarjeta implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("numeroEnmascarado")
    private String numeroEnmascarado;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("nombreTitular")
    private String nombreTitular;

    public long getId() {
        return id;
    }

    public String getNumeroEnmascarado() {
        return numeroEnmascarado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }
}
