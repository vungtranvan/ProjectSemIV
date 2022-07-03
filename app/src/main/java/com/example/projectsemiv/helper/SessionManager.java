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
    public static final String KEY_USER_Id = "userId";
    public static final String KEY_NAME_DISPLAY_USER = "nameDisplayUser";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_ROLE = "role";

    public SessionManager(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences(USERS_SESSION, Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String userId, String nameDisplay, String imageUser, String role) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_Id, userId);
        editor.putString(KEY_NAME_DISPLAY_USER, nameDisplay);
        editor.putString(KEY_USER_IMAGE, imageUser);
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    public HashMap<String, String> getUserInfoInSession() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_USER_IMAGE, userSession.getString(KEY_USER_IMAGE, null));
        userData.put(KEY_NAME_DISPLAY_USER, userSession.getString(KEY_NAME_DISPLAY_USER, null));
        userData.put(KEY_USER_IMAGE, userSession.getString(KEY_USER_IMAGE, null));
        return userData;
    }

    public int getUserIdInSession() {
        return Integer.parseInt(userSession.getString(KEY_USER_Id, null));
    }

    public boolean getRoleUserInSession() {
        return Boolean.parseBoolean(userSession.getString(KEY_ROLE, null));
    }

    public void setNameDisplayAndImageUserInSession(String name, String image) {
        if (name != null && name.length() > 0) {
            editor.remove(KEY_NAME_DISPLAY_USER);
            editor.putString(KEY_NAME_DISPLAY_USER, name);
        }
        if (image != null && image.length() > 0) {
            editor.remove(KEY_USER_IMAGE);
            editor.putString(KEY_USER_IMAGE, image);
        }
        editor.commit();
    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        }
        return false;
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}
