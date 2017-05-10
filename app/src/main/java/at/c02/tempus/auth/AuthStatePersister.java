package at.c02.tempus.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import net.openid.appauth.AuthState;

import org.json.JSONException;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Daniel Hartl on 04.05.2017.
 */

public class AuthStatePersister {

    private static final String TAG = "AuthStatePersister";
    private static final String AUTH_PREF = "auth";
    private static final String AUTH_PREF_STATE_JSON = "stateJson";
    private static final String AUTH_PREF_USER_NAME = "userName";

    @Inject
    protected Context context;

    @Inject
    public AuthStatePersister() {

    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(AUTH_PREF, MODE_PRIVATE);
    }

    @NonNull
    public AuthState readAuthState() {
        SharedPreferences authPrefs = getSharedPreferences();
        String stateJson = authPrefs.getString(AUTH_PREF_STATE_JSON, null);
        if (stateJson != null) {
            try {
                return AuthState.jsonDeserialize(stateJson);
            } catch (JSONException e) {
                Log.e(TAG, "Fehler bei der Deserialisierung des AuthStates: " + stateJson, e);
            }
        }
        return new AuthState();
    }

    public void writeAuthState(AuthState state) {
        SharedPreferences authPrefs = context.getSharedPreferences(AUTH_PREF, MODE_PRIVATE);
        if(state != null) {
            authPrefs.edit()
                    .putString(AUTH_PREF_STATE_JSON, state.jsonSerializeString())
                    .apply();
        }else{
            authPrefs.edit().clear().commit();
        }
    }

    public String readUserName() {
        SharedPreferences authPrefs = getSharedPreferences();
        return authPrefs.getString(AUTH_PREF_USER_NAME, null);

    }

    public void writeUserName(String userName) {
        SharedPreferences authPrefs = context.getSharedPreferences(AUTH_PREF, MODE_PRIVATE);
        if (userName != null) {
            authPrefs.edit()
                    .putString(AUTH_PREF_USER_NAME, userName)
                    .apply();
        } else {
            authPrefs.edit().clear().commit();
        }
    }
}
