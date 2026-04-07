package com.student.healthapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://10.0.2.2:8080/login";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("auth", MODE_PRIVATE);
        String token = sp.getString("token", null);
        if (!TextUtils.isEmpty(token)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }


        EditText etAccount = findViewById(R.id.etAccount);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String account = etAccount.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(account)) {
                Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText("登录中...");

            new Thread(() -> doLogin(account, password, btnLogin)).start();
        });
    }

    private void doLogin(String account, String password, Button btnLogin) {
        try {
            JSONObject json = new JSONObject();
            json.put("account", account);
            json.put("password", password);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(LOGIN_URL)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String resp = response.body().string();
                    String token = parseToken(resp);
                    if (!TextUtils.isEmpty(token)) {
                        onLoginSuccess(token);
                        return;
                    }
                }
            }

            onLoginFailed("账号或密码错误", btnLogin);

        } catch (Exception e) {
            // 后端未就绪时测试兜底账号
            if ("test".equals(account) && "123456".equals(password)) {
                onLoginSuccess("mock-token-123");
            } else {
                onLoginFailed("网络异常，请稍后重试", btnLogin);
            }
        }
    }

    private String parseToken(String responseJson) {
        try {
            JSONObject root = new JSONObject(responseJson);
            if (root.has("data")) {
                JSONObject data = root.getJSONObject("data");
                if (data.has("token")) return data.getString("token");
            }
            if (root.has("token")) return root.getString("token");
        } catch (Exception ignored) {
        }
        return null;
    }

    private void onLoginSuccess(String token) {
        runOnUiThread(() -> {
            SharedPreferences sp = getSharedPreferences("auth", MODE_PRIVATE);
            sp.edit().putString("token", token).apply();

            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }

    private void onLoginFailed(String message, Button btnLogin) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            btnLogin.setEnabled(true);
            btnLogin.setText("登录");
        });
    }
}
