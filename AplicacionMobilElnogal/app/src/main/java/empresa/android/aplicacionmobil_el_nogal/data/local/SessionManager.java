package empresa.android.aplicacionmobil_el_nogal.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import empresa.android.aplicacionmobil_el_nogal.data.model.Usuario;

public class SessionManager {

    private static final String PREF_NAME = "nogal_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULLNAME = "fullname";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUsuario(Usuario usuario) {
        preferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putLong(KEY_USER_ID, usuario.getId())
                .putString(KEY_USERNAME, usuario.getUsername())
                .putString(KEY_FULLNAME, usuario.getNombreCompleto())
                .apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public long getUsuarioId() {
        return preferences.getLong(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    public String getNombreCompleto() {
        return preferences.getString(KEY_FULLNAME, "");
    }

    public void clearSession() {
        preferences.edit().clear().apply();
    }
}
