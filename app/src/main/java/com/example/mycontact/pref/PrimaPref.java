package com.example.mycontact.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mycontact.model.User;

public class PrimaPref {

    public static String PREF_NAME = "prima_shared_pref";
    public static String ID_KEY = "user_id";
    public static String NAME_KEY = "user_name";
    public static String EMAIL_KEY = "user_email";
    public static String PHOTO_KEY = "user_photo";

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    public PrimaPref(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setUser(User user) {
        editor = pref.edit();

        editor.putString(ID_KEY, user.getId());
        editor.putString(NAME_KEY, user.getName());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(PHOTO_KEY, user.getPhoto());
        editor.commit();
    }

    public User  getUser() {
        User user = new User();
        user.setId(pref.getString(ID_KEY, null));
        user.setName(pref.getString(NAME_KEY, null));
        user.setEmail(pref.getString(EMAIL_KEY, null));
        user.setPhoto(pref.getString(PHOTO_KEY, null));

        return user;
    }

    public void removeUser() {
        editor = pref.edit();

        editor.remove(ID_KEY);
        editor.remove(NAME_KEY);
        editor.remove(EMAIL_KEY);
        editor.remove(PHOTO_KEY);
        editor.commit();
    }

}
