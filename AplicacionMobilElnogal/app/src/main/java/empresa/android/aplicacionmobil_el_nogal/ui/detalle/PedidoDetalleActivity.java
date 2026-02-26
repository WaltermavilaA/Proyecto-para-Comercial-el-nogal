package empresa.android.aplicacionmobil_el_nogal.ui.detalle;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import empresa.android.aplicacionmobil_el_nogal.R;
import empresa.android.aplicacionmobil_el_nogal.data.model.DetallePedido;
import empresa.android.aplicacionmobil_el_nogal.data.model.Pedido;
import empresa.android.aplicacionmobil_el_nogal.data.model.Producto;
import empresa.android.aplicacionmobil_el_nogal.databinding.ActivityPedidoDetalleBinding;
import empresa.android.aplicacionmobil_el_nogal.databinding.ItemDetalleProductoBinding;
import empresa.android.aplicacionmobil_el_nogal.ui.pedidos.PedidosActivity;

public class PedidoDetalleActivity extends AppCompatActivity {

    private ActivityPedidoDetalleBinding binding;
    private Pedido pedido;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPedidoDetalleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pedido = (Pedido) getIntent().getSerializableExtra(PedidosActivity.EXTRA_PEDIDO);
        if (pedido == null) {
            Toast.makeText(this, R.string.error_cargar_detalle, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupToolbar();
        bindPedido();
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.toolbar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void bindPedido() {
        binding.tvNumeroPedido.setText(pedido.getNumeroPedido());
        binding.tvFechaPedido.setText(formatFecha(pedido.getFechaPedido()));

        String estado = pedido.getEstado();
        binding.chipEstado.setText(formatearEstado(estado));
        binding.chipEstado.setChipBackgroundColorResource(colorPorEstado(estado));
        actualizarTimeline(estado);

        bindDireccion();
        bindCodigoVerificacion();

        binding.tvMetodoPago.setText(obtenerMetodoPagoDetalle());

        binding.productsContainer.removeAllViews();
        if (pedido.getDetalles() != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (DetallePedido detalle : pedido.getDetalles()) {
                ItemDetalleProductoBinding itemBinding = ItemDetalleProductoBinding.inflate(inflater, binding.productsContainer, false);
                Producto producto = detalle.getProducto();
                if (producto != null) {
                    itemBinding.tvNombreProducto.setText(producto.getNombre());
                }
                itemBinding.tvCantidad.setText(getString(R.string.detalle_cantidad, detalle.getCantidad()));
                itemBinding.tvSubtotal.setText(String.format(Locale.getDefault(), "S/ %.2f", detalle.getSubtotal()));
                binding.productsContainer.addView(itemBinding.getRoot());
            }
        }

        binding.tvSubtotal.setText(String.format(Locale.getDefault(), "S/ %.2f", pedido.getSubtotal()));
        binding.tvEnvio.setText(formatearEnvio());
        binding.tvTotal.setText(String.format(Locale.getDefault(), "S/ %.2f", pedido.getTotal()));
    }

    private void bindDireccion() {
        if (pedido.getDireccion() == null) {
            binding.tvNombreReceptor.setText(R.string.detalle_sin_direccion);
            binding.tvDireccionEnvio.setText("");
            binding.tvTelefonoContacto.setVisibility(View.GONE);
            return;
        }

        String nombre = (pedido.getDireccion().getNombreReceptor() == null ? "" : pedido.getDireccion().getNombreReceptor());
        String apellidos = (pedido.getDireccion().getApellidosReceptor() == null ? "" : pedido.getDireccion().getApellidosReceptor());
        String nombreCompleto = (nombre + " " + apellidos).trim();
        binding.tvNombreReceptor.setText(nombreCompleto.isEmpty() ? getString(R.string.detalle_sin_direccion) : nombreCompleto);
        binding.tvDireccionEnvio.setText(pedido.getDireccion().getDireccionCompleta());

        String telefono = pedido.getDireccion().getTelefono();
        if (!TextUtils.isEmpty(telefono)) {
            binding.tvTelefonoContacto.setText(getString(R.string.detalle_contacto_telefono, telefono));
            binding.tvTelefonoContacto.setVisibility(View.VISIBLE);
        } else {
            binding.tvTelefonoContacto.setVisibility(View.GONE);
        }
    }

    private String formatFecha(String fecha) {
        if (fecha == null) return "";
        try {
            Date date = inputFormat.parse(fecha);
            if (date != null) {
                return outputFormat.format(date);
            }
        } catch (ParseException ignored) {
        }
        return fecha;
    }

    private void actualizarTimeline(String estado) {
        boolean step1 = false;
        boolean step2 = false;
        boolean step3 = false;
        boolean step4 = false;
        boolean cancelado = false;

        if (estado != null) {
            String upper = estado.toUpperCase(Locale.getDefault());
            switch (upper) {
                case "PENDIENTE":
                    step1 = true;
                    break;
                case "PROCESANDO":
                    step1 = true;
                    step2 = true;
                    break;
                case "ENVIADO":
                    step1 = true;
                    step2 = true;
                    step3 = true;
                    break;
                case "ENTREGADO":
                    step1 = true;
                    step2 = true;
                    step3 = true;
                    step4 = true;
                    break;
                case "CANCELADO":
                    cancelado = true;
                    break;
                default:
                    step1 = true;
                    break;
            }
        }

        marcarPaso(binding.step1Circle, binding.step1Label, step1, cancelado);
        marcarPaso(binding.step2Circle, binding.step2Label, step2, cancelado);
        marcarPaso(binding.step3Circle, binding.step3Label, step3, cancelado);
        marcarPaso(binding.step4Circle, binding.step4Label, step4, cancelado);

        pintarLinea(binding.line1, step2, cancelado);
        pintarLinea(binding.line2, step3, cancelado);
        pintarLinea(binding.line3, step4, cancelado);
    }

    private void marcarPaso(ImageView circle, TextView label, boolean activo, boolean cancelado) {
        int colorActivo = cancelado ? R.color.estado_cancelado : R.color.nogal_primary;
        int colorInactivo = android.R.color.darker_gray;
        circle.setBackgroundResource(activo ? R.drawable.bg_timeline_circle_active : R.drawable.bg_timeline_circle_inactive);
        circle.getBackground().setTint(ContextCompat.getColor(this, activo ? colorActivo : colorInactivo));
        label.setTextColor(ContextCompat.getColor(this, activo ? colorActivo : colorInactivo));
    }

    private void pintarLinea(View line, boolean activo, boolean cancelado) {
        int colorActivo = cancelado ? R.color.estado_cancelado : R.color.nogal_primary;
        int colorInactivo = android.R.color.darker_gray;
        line.setBackgroundColor(ContextCompat.getColor(this, activo ? colorActivo : colorInactivo));
    }

    private String formatearEstado(String estado) {
        if (estado == null) return "";
        switch (estado.toUpperCase(Locale.getDefault())) {
            case "PENDIENTE":
                return getString(R.string.estado_en_preparacion);
            case "PROCESANDO":
                return getString(R.string.estado_procesando);
            case "ENVIADO":
                return getString(R.string.estado_en_camino);
            case "ENTREGADO":
                return getString(R.string.estado_finalizado);
            case "CANCELADO":
                return getString(R.string.estado_cancelado);
            default:
                return estado;
        }
    }

    private int colorPorEstado(String estado) {
        if (estado == null) return R.color.nogal_primary;
        switch (estado.toUpperCase(Locale.getDefault())) {
            case "PENDIENTE":
                return R.color.estado_en_preparacion;
            case "PROCESANDO":
                return R.color.estado_procesando;
            case "ENVIADO":
                return R.color.estado_en_camino;
            case "ENTREGADO":
                return R.color.estado_finalizado;
            case "CANCELADO":
                return R.color.estado_cancelado;
            default:
                return R.color.nogal_primary;
        }
    }

    private void bindCodigoVerificacion() {
        String codigo = pedido.getCodigoVerificacion();
        if (TextUtils.isEmpty(codigo)) {
            binding.tvCodigoVerificacion.setText(R.string.detalle_codigo_no_disponible);
            binding.tvCodigoInstruccion.setVisibility(View.GONE);
        } else {
            binding.tvCodigoVerificacion.setText(codigo);
            binding.tvCodigoInstruccion.setVisibility(View.VISIBLE);
            binding.tvCodigoInstruccion.setText(R.string.detalle_codigo_instruccion);
        }
    }

    private String obtenerMetodoPagoDetalle() {
        if (!TextUtils.isEmpty(pedido.getMetodoPago())) {
            return pedido.getMetodoPago();
        }
        if (pedido.getTarjeta() != null && !TextUtils.isEmpty(pedido.getTarjeta().getTipo())) {
            return getString(R.string.detalle_metodo_tarjeta_formato,
                    pedido.getTarjeta().getTipo(),
                    pedido.getTarjeta().getNumeroEnmascarado());
        }
        return getString(R.string.detalle_sin_metodo_pago);
    }

    private String formatearEnvio() {
        if (pedido.getEnvio() <= 0) {
            return getString(R.string.detalle_envio_gratis);
        }
        return String.format(Locale.getDefault(), "S/ %.2f", pedido.getEnvio());
    }
}
