package empresa.android.aplicacionmobil_el_nogal;

import android.app.Application;

import empresa.android.aplicacionmobil_el_nogal.data.local.SessionManager;

public class NogalApp extends Application {
    private static SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = new SessionManager(this);
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }
}
