package empresa.android.aplicacionmobil_el_nogal.ui.pedidos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import empresa.android.aplicacionmobil_el_nogal.NogalApp;
import empresa.android.aplicacionmobil_el_nogal.data.local.SessionManager;
import empresa.android.aplicacionmobil_el_nogal.data.model.Pedido;
import empresa.android.aplicacionmobil_el_nogal.data.repository.PedidoRepository;
import empresa.android.aplicacionmobil_el_nogal.databinding.ActivityPedidosBinding;
import empresa.android.aplicacionmobil_el_nogal.ui.detalle.PedidoDetalleActivity;
import empresa.android.aplicacionmobil_el_nogal.ui.login.LoginActivity;

public class PedidosActivity extends AppCompatActivity implements PedidosAdapter.OnPedidoClickListener {

    public static final String EXTRA_PEDIDO = "extra_pedido";

    private ActivityPedidosBinding binding;
    private PedidosAdapter adapter;
    private PedidoRepository repository;
    private SessionManager sessionManager;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPedidosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = NogalApp.getSessionManager();
        if (sessionManager == null || !sessionManager.isLoggedIn()) {
            navigateToLogin();
            finish();
            return;
        }

        repository = new PedidoRepository();
        adapter = new PedidosAdapter(this);

        setupToolbar();
        setupRecycler();
        setupActions();

        cargarPedidos(false);
    }

    private void setupToolbar() {
        binding.toolbar.setNavigationIcon(null);
        binding.toolbar.setTitle(empresa.android.aplicacionmobil_el_nogal.R.string.pedidos_title);
        binding.tvSaludo.setText(getString(empresa.android.aplicacionmobil_el_nogal.R.string.pedidos_saludo,
                sessionManager.getNombreCompleto()));
    }

    private void setupRecycler() {
        binding.recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerPedidos.setAdapter(adapter);
    }

    private void setupActions() {
        binding.btnCerrarSesion.setOnClickListener(v -> {
            if (sessionManager != null) {
                sessionManager.clearSession();
            }
            navigateToLogin();
            finish();
        });

        binding.swipeRefresh.setOnRefreshListener(() -> cargarPedidos(true));
    }

    private void cargarPedidos(boolean refrescoManual) {
        showLoading(!refrescoManual);
        executor.execute(() -> {
            try {
                List<Pedido> pedidos = repository.obtenerPedidos(sessionManager.getUsuarioId());
                mainHandler.post(() -> {
                    showLoading(false);
                    binding.swipeRefresh.setRefreshing(false);
                    adapter.setPedidos(pedidos);
                    binding.tvEmpty.setVisibility(pedidos == null || pedidos.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (IOException e) {
                mainHandler.post(() -> {
                    showLoading(false);
                    binding.swipeRefresh.setRefreshing(false);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    Toast.makeText(this,
                            empresa.android.aplicacionmobil_el_nogal.R.string.error_cargar_pedidos,
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.recyclerPedidos.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onPedidoClick(Pedido pedido) {
        Intent intent = new Intent(this, PedidoDetalleActivity.class);
        intent.putExtra(EXTRA_PEDIDO, pedido);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
