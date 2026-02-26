package empresa.android.aplicacionmobil_el_nogal.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import empresa.android.aplicacionmobil_el_nogal.NogalApp;
import empresa.android.aplicacionmobil_el_nogal.data.local.SessionManager;
import empresa.android.aplicacionmobil_el_nogal.data.model.Usuario;
import empresa.android.aplicacionmobil_el_nogal.data.repository.PedidoRepository;
import empresa.android.aplicacionmobil_el_nogal.databinding.ActivityLoginBinding;
import empresa.android.aplicacionmobil_el_nogal.ui.pedidos.PedidosActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PedidoRepository repository;
    private SessionManager sessionManager;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new PedidoRepository();
        sessionManager = NogalApp.getSessionManager();

        if (sessionManager != null && sessionManager.isLoggedIn()) {
            navigateToPedidos();
            finish();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String username = binding.etUsername.getText() != null
                ? binding.etUsername.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString().trim() : "";

        boolean valid = true;
        if (TextUtils.isEmpty(username)) {
            binding.tilUsername.setError(getString(empresa.android.aplicacionmobil_el_nogal.R.string.error_required));
            valid = false;
        } else {
            binding.tilUsername.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError(getString(empresa.android.aplicacionmobil_el_nogal.R.string.error_required));
            valid = false;
        } else {
            binding.tilPassword.setError(null);
        }

        if (!valid) {
            return;
        }

        showLoading(true);
        binding.tvError.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                Usuario usuario = repository.login(username, password);
                if (sessionManager != null) {
                    sessionManager.saveUsuario(usuario);
                }
                mainHandler.post(() -> {
                    showLoading(false);
                    navigateToPedidos();
                    finish();
                });
            } catch (IOException e) {
                mainHandler.post(() -> {
                    showLoading(false);
                    showError(getString(empresa.android.aplicacionmobil_el_nogal.R.string.error_login));
                });
            }
        });
    }

    private void showLoading(boolean loading) {
        binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!loading);
    }

    private void showError(String message) {
        binding.tvError.setText(message);
        binding.tvError.setVisibility(View.VISIBLE);
    }

    private void navigateToPedidos() {
        Intent intent = new Intent(this, PedidosActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
