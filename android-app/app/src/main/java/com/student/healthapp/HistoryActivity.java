package com.student.healthapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    private TextView tvHistoryDate;
    private TextView tvHistoryWater;
    private TextView tvHistorySleep;
    private TextView tvHistoryExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvHistoryDate = findViewById(R.id.tvHistoryDate);
        tvHistoryWater = findViewById(R.id.tvHistoryWater);
        tvHistorySleep = findViewById(R.id.tvHistorySleep);
        tvHistoryExercise = findViewById(R.id.tvHistoryExercise);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLatestRecordFromLocal();
        fetchHistoryFromServer();
    }

    private void loadLatestRecordFromLocal() {
        SharedPreferences sp = getSharedPreferences("health_data", MODE_PRIVATE);
        String savedDate = sp.getString("today_date", "");
        int water = sp.getInt("today_water", 0);
        float sleep = sp.getFloat("today_sleep", 0.0f);
        int exercise = sp.getInt("today_exercise", 0);

        if (savedDate.isEmpty()) {
            tvHistoryDate.setText("日期：暂无记录");
            tvHistoryWater.setText("饮水：0 杯");
            tvHistorySleep.setText("睡眠：0.0 小时");
            tvHistoryExercise.setText("运动：0 分钟");
            return;
        }

        tvHistoryDate.setText("日期：" + savedDate);
        tvHistoryWater.setText("饮水：" + water + " 杯");
        tvHistorySleep.setText("睡眠：" + sleep + " 小时");
        tvHistoryExercise.setText("运动：" + exercise + " 分钟");
    }

    private void fetchHistoryFromServer() {
        // TODO: 后端接口完成后，在这里调用 GET /history。
    }
}
