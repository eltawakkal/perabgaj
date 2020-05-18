package com.example.mycontact.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontact.MainActivity;
import com.example.mycontact.R;
import com.example.mycontact.model.User;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.example.mycontact.pref.PrimaPref;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ApiEndPoint apiEndPoint;
    protected PrimaPref pref;
    private User user;
    private ImageView imgProfile;
    private EditText edtName, edtEmail, edtPass;
    private MaterialButton mtbtSignup;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private String userDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    private void initView() {
        user = getIntent().getParcelableExtra(LoginActivity.USER_KEY);
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);
        pref = new PrimaPref(this);
        userDevice = getDeviceName();

        imgProfile = findViewById(R.id.img_profile_signup);
        edtName = findViewById(R.id.edt_name_signup);
        edtEmail = findViewById(R.id.edt_email_signup);
        edtPass = findViewById(R.id.edt_pass_signup);
        mtbtSignup = findViewById(R.id.mtbt_signup);
        tvLogin = findViewById(R.id.tv_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Tunggu");
        progressDialog.setMessage("Sedang Mendaftar...");
        progressDialog.setCancelable(false);

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        mtbtSignup.setOnClickListener(v -> {
            if (edtPass.getText().length() > 16) {
                Toast.makeText(this, "Sandi lebih dari 16 karakter", Toast.LENGTH_SHORT).show();
                edtPass.requestFocus();
                edtPass.selectAll();
            } else {
                addUserToServer();
            }
        });

        setBindData();
    }

    void setBindData() {
        try {
            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.user).into(imgProfile);
        } catch (Exception e){
            Picasso.get().load(R.drawable.user).into(imgProfile);
        }

        edtName.setText(user.getName());
        edtEmail.setText(user.getEmail());
    }

    void addUserToServer() {
        progressDialog.show();

        String name = edtName.getText().toString();
        String pass = edtPass.getText().toString();
        Call<User> addUserToServer = apiEndPoint.addUser(user.getId(), name, user.getEmail(), pass, userDevice, user.getPhoto());
        addUserToServer.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(SignUpActivity.this, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show();
                updateUI();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("kesalahanSignUp", t.getMessage());
                Toast.makeText(SignUpActivity.this, "Kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    void updateUI() {
        User userToPref = new User();
        userToPref.setId(user.getId());
        userToPref.setName(edtName.getText().toString());
        userToPref.setEmail(user.getEmail());
        userToPref.setPassword(edtPass.getText().toString());
        userToPref.setPhoto(user.getPhoto());

        pref.setUser(userToPref);
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
