package com.example.projectsemiv.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String USERS_SESSION = "usersLoginSession";

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_NAME_USER_DISPLAY = "nameUserDisplay";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_ROLE = "role";

    public SessionManager(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences(USERS_SESSION, Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String userName, String nameDisplay, String imageUser, String role) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_NAME_USER_DISPLAY, nameDisplay);
        editor.putString(KEY_USER_IMAGE, imageUser);
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    public HashMap<String, String> getUserInfoInSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_USER_NAME, userSession.getString(KEY_USER_NAME, null));
        userData.put(KEY_ROLE, userSession.getString(KEY_ROLE, null));
        userData.put(KEY_USER_IMAGE, userSession.getString(KEY_USER_IMAGE, null));
        userData.put(KEY_NAME_USER_DISPLAY, userSession.getString(KEY_NAME_USER_DISPLAY, null));
        return userData;
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        }
        return false;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }
}
