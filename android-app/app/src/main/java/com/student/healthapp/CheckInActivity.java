package com.student.healthapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckInActivity extends AppCompatActivity {

    private EditText etWater;
    private EditText etSleep;
    private EditText etExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        etWater = findViewById(R.id.etWater);
        etSleep = findViewById(R.id.etSleep);
        etExercise = findViewById(R.id.etExercise);
        Button btnSubmit = findViewById(R.id.btnSubmitCheckIn);

        btnSubmit.setOnClickListener(v -> submitCheckIn());
    }

    private void submitCheckIn() {
        String waterStr = etWater.getText().toString().trim();
        String sleepStr = etSleep.getText().toString().trim();
        String exerciseStr = etExercise.getText().toString().trim();

        if (TextUtils.isEmpty(waterStr) || TextUtils.isEmpty(sleepStr) || TextUtils.isEmpty(exerciseStr)) {
            Toast.makeText(this, "请完整填写打卡信息", Toast.LENGTH_SHORT).show();
            return;
        }

        int water;
        float sleep;
        int exercise;

        try {
            water = Integer.parseInt(waterStr);
            sleep = Float.parseFloat(sleepStr);
            exercise = Integer.parseInt(exerciseStr);
        } catch (Exception e) {
            Toast.makeText(this, "请输入正确的数字格式", Toast.LENGTH_SHORT).show();
            return;
        }

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        uploadCheckInToServer(water, sleep, exercise, today);
    }

    private void uploadCheckInToServer(int water, float sleep, int exercise, String today) {
        SharedPreferences sp = getSharedPreferences("health_data", MODE_PRIVATE);
        sp.edit()
                .putString("today_date", today)
                .putInt("today_water", water)
                .putFloat("today_sleep", sleep)
                .putInt("today_exercise", exercise)
                .apply();

        // TODO 后端就绪后接入 POST /checkin。当前先写本地，保证离线也能看到数据。
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
