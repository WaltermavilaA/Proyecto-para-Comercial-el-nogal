package empresa.android.aplicacionmobil_el_nogal.data.remote;

import java.util.List;

import empresa.android.aplicacionmobil_el_nogal.data.model.Pedido;
import empresa.android.aplicacionmobil_el_nogal.data.model.Usuario;
import empresa.android.aplicacionmobil_el_nogal.data.remote.dto.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface NogalApiService {

    @POST("usuario/login")
    Call<Usuario> login(@Body LoginRequest request);

    @GET("pedido/usuario/{usuarioId}")
    Call<List<Pedido>> obtenerPedidosPorUsuario(@Path("usuarioId") long usuarioId);
}
