package com.student.healthapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvDate;
    private TextView tvStreak;
    private TextView tvWater;
    private TextView tvSleep;
    private TextView tvExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvDate = findViewById(R.id.tvDate);
        tvStreak = findViewById(R.id.tvStreak);
        tvWater = findViewById(R.id.tvWater);
        tvSleep = findViewById(R.id.tvSleep);
        tvExercise = findViewById(R.id.tvExercise);

        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnCheckIn = findViewById(R.id.btnCheckIn);
        Button btnHistory = findViewById(R.id.btnHistory);

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("auth", MODE_PRIVATE).edit().remove("token").apply();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        btnCheckIn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CheckInActivity.class))
        );

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSummaryFromLocal();
        syncSummaryFromServer();
    }

    private void loadSummaryFromLocal() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        tvDate.setText(today);

        SharedPreferences sp = getSharedPreferences("health_data", MODE_PRIVATE);
        String savedDate = sp.getString("today_date", "");
        int water = 0;
        float sleep = 0.0f;
        int exercise = 0;

        if (today.equals(savedDate)) {
            water = sp.getInt("today_water", 0);
            sleep = sp.getFloat("today_sleep", 0.0f);
            exercise = sp.getInt("today_exercise", 0);
        }

        int streak = (water > 0 || sleep > 0 || exercise > 0) ? 1 : 0;
        tvStreak.setText("连续打卡：" + streak + " 天");
        tvWater.setText("💧  饮水：" + water + " / 8 杯");
        tvSleep.setText("🛌  睡眠：" + sleep + " 小时");
        tvExercise.setText("🏃  运动：" + exercise + " 分钟");
    }

    private void syncSummaryFromServer() {
        // TODO 后端就绪后接入 GET /summary/today，并在 runOnUiThread 中刷新页面。
    }
}
