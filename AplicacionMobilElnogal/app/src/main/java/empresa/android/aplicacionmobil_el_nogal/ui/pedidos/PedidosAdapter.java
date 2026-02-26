package empresa.android.aplicacionmobil_el_nogal.ui.pedidos;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import empresa.android.aplicacionmobil_el_nogal.R;
import empresa.android.aplicacionmobil_el_nogal.data.model.Pedido;
import empresa.android.aplicacionmobil_el_nogal.databinding.ItemPedidoBinding;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedido pedido);
    }

    private final List<Pedido> pedidos = new ArrayList<>();
    private final OnPedidoClickListener listener;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public PedidosAdapter(OnPedidoClickListener listener) {
        this.listener = listener;
    }

    public void setPedidos(List<Pedido> nuevosPedidos) {
        pedidos.clear();
        if (nuevosPedidos != null) {
            pedidos.addAll(nuevosPedidos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPedidoBinding binding = ItemPedidoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PedidoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.bind(pedido);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    class PedidoViewHolder extends RecyclerView.ViewHolder {
        private final ItemPedidoBinding binding;

        PedidoViewHolder(ItemPedidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Pedido pedido) {
            binding.tvNumeroPedido.setText(pedido.getNumeroPedido());
            binding.tvFecha.setText(formatFecha(pedido.getFechaPedido()));
            binding.tvTotal.setText(String.format(Locale.getDefault(), "S/ %.2f", pedido.getTotal()));

            binding.tvMetodoPago.setText(obtenerMetodoPagoTexto(pedido));

            binding.chipEstado.setText(formatearEstado(pedido.getEstado()));
            binding.chipEstado.setChipBackgroundColorResource(colorPorEstado(binding.getRoot().getContext(), pedido.getEstado()));

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPedidoClick(pedido);
                }
            });
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

        private String formatearEstado(String estado) {
            if (estado == null) return "";
            String estadoUpper = estado.toUpperCase(Locale.getDefault());
            switch (estadoUpper) {
                case "PENDIENTE":
                    return binding.getRoot().getContext().getString(R.string.estado_en_preparacion);
                case "PROCESANDO":
                    return binding.getRoot().getContext().getString(R.string.estado_procesando);
                case "ENVIADO":
                    return binding.getRoot().getContext().getString(R.string.estado_en_camino);
                case "ENTREGADO":
                    return binding.getRoot().getContext().getString(R.string.estado_finalizado);
                case "CANCELADO":
                    return binding.getRoot().getContext().getString(R.string.estado_cancelado);
                default:
                    return estado;
            }
        }

        private int colorPorEstado(Context context, String estado) {
            if (estado == null) return R.color.nogal_primary;
            String estadoUpper = estado.toUpperCase(Locale.getDefault());
            switch (estadoUpper) {
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

        private String obtenerMetodoPagoTexto(Pedido pedido) {
            if (!TextUtils.isEmpty(pedido.getMetodoPago())) {
                return pedido.getMetodoPago();
            }
            if (pedido.getTarjeta() != null && !TextUtils.isEmpty(pedido.getTarjeta().getTipo())) {
                return binding.getRoot().getContext().getString(
                        R.string.detalle_metodo_tarjeta_formato,
                        pedido.getTarjeta().getTipo(),
                        pedido.getTarjeta().getNumeroEnmascarado()
                );
            }
            return binding.getRoot().getContext().getString(R.string.detalle_sin_metodo_pago);
        }
    }
}
