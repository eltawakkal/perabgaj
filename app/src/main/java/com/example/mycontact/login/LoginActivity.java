package com.example.mycontact.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.mycontact.MainActivity;
import com.example.mycontact.R;
import com.example.mycontact.model.User;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.example.mycontact.pref.PrimaPref;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static String USER_KEY = "userKey";
    private static int G_LOGIN_CODE = 12;
    private PrimaPref pref;
    private ApiEndPoint apiEndPoint;
    private GoogleSignInOptions mGoogleSignInOpt;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount mAccount;
    private String userDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();

        if (pref.getUser().getId() != null) {
            updateUi();
        } else {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, G_LOGIN_CODE);
        }
    }

    private void initview() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);
        pref = new PrimaPref(this);
        userDevice = getDeviceName();

        mGoogleSignInOpt = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOpt);
        mAccount = GoogleSignIn.getLastSignedInAccount(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G_LOGIN_CODE) {
            Task task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleResult(task);
            mGoogleSignInClient.signOut();
        }
    }

    private void handleResult(Task<GoogleSignInAccount> task) {
        try {
            mAccount = task.getResult(ApiException.class);
            checkUser(mAccount.getId());
        } catch (Exception e) {
           Log.d("gagalLogin", e.getMessage());
        }
    }

    void setUser(User user) {
        pref.setUser(user);
        updateUi();
    }

    private void updateUi() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    void checkUser(String id) {
        Call<User> checkUser = apiEndPoint.checkUser(id);
        checkUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                if (user.getId() != null) {

                    if (user.getDevice().equals(userDevice)) {
                        setUser(user);
                    } else {
                        Toast.makeText(LoginActivity.this, "Perangkat Anda telah terdaftar di perangkat lain", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String photoUrl = "";
                    if (mAccount.getPhotoUrl() != null) {
                        photoUrl = mAccount.getPhotoUrl().toString();
                    }
                    User userFromGoogle = new User(mAccount.getId(), "", mAccount.getDisplayName(), mAccount.getEmail(), "", user.getDevice(), photoUrl);
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    intent.putExtra(USER_KEY, userFromGoogle);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("gagalLoginServer", t.getMessage());
            }
        });
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
}
