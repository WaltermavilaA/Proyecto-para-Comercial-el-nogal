package empresa.android.aplicacionmobil_el_nogal.data.repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import empresa.android.aplicacionmobil_el_nogal.data.model.Pedido;
import empresa.android.aplicacionmobil_el_nogal.data.model.Usuario;
import empresa.android.aplicacionmobil_el_nogal.data.remote.NogalApiService;
import empresa.android.aplicacionmobil_el_nogal.data.remote.RetrofitClient;
import empresa.android.aplicacionmobil_el_nogal.data.remote.dto.LoginRequest;
import retrofit2.Call;
import retrofit2.Response;

public class PedidoRepository {

    private final NogalApiService apiService;

    public PedidoRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public Usuario login(String username, String password) throws IOException {
        Call<Usuario> call = apiService.login(new LoginRequest(username, password));
        Response<Usuario> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        throw new IOException("Error en login: " + response.code());
    }

    public List<Pedido> obtenerPedidos(long usuarioId) throws IOException {
        Call<List<Pedido>> call = apiService.obtenerPedidosPorUsuario(usuarioId);
        Response<List<Pedido>> response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        }
        if (!response.isSuccessful()) {
            throw new IOException("Error obteniendo pedidos: " + response.code());
        }
        return Collections.emptyList();
    }
}
