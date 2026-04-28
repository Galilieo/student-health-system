package com.student.healthapp;

import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileActivity extends AppCompatActivity {

    private SharedPreferences spProfile;
    private SharedPreferences spAuth;

    private TextView tvAvatarLetter, tvUserId;
    private LinearLayout contentProfile, contentGoal, contentReminder;
    private TextView arrowProfile, arrowGoal, arrowReminder;
    private TextView tvAge, tvHeight, tvWeight, tvBirthday;
    private TextView tvGoalWater, tvGoalSleep, tvGoalExercise;
    private TextView tvReminderWater, tvReminderSleep, tvReminderExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        spProfile = getSharedPreferences("profile_data", MODE_PRIVATE);
        spAuth = getSharedPreferences("auth", MODE_PRIVATE);

        initViews();
        loadUserData();
        setupExpandCollapse();
        setupRowClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshAllDisplays();
    }

    private void initViews() {
        tvAvatarLetter = findViewById(R.id.tvAvatarLetter);
        tvUserId = findViewById(R.id.tvUserId);
        contentProfile = findViewById(R.id.contentProfile);
        contentGoal = findViewById(R.id.contentGoal);
        contentReminder = findViewById(R.id.contentReminder);
        arrowProfile = findViewById(R.id.arrowProfile);
        arrowGoal = findViewById(R.id.arrowGoal);
        arrowReminder = findViewById(R.id.arrowReminder);
        tvAge = findViewById(R.id.tvAge);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvGoalWater = findViewById(R.id.tvGoalWater);
        tvGoalSleep = findViewById(R.id.tvGoalSleep);
        tvGoalExercise = findViewById(R.id.tvGoalExercise);
        tvReminderWater = findViewById(R.id.tvReminderWater);
        tvReminderSleep = findViewById(R.id.tvReminderSleep);
        tvReminderExercise = findViewById(R.id.tvReminderExercise);
    }

    private void loadUserData() {
        String account = spAuth.getString("account", "User");
        if (!TextUtils.isEmpty(account)) {
            tvAvatarLetter.setText(account.substring(0, 1).toUpperCase());
            tvUserId.setText("用户ID：" + account);
        } else {
            tvAvatarLetter.setText("U");
            tvUserId.setText("用户ID：未登录");
        }
    }

    private void refreshAllDisplays() {
        tvAge.setText(spProfile.getString("age", "未设置") + " 岁");
        tvHeight.setText(spProfile.getString("height", "未设置") + " cm");
        tvWeight.setText(spProfile.getString("weight", "未设置") + " kg");
        tvBirthday.setText(TextUtils.isEmpty(spProfile.getString("birthday", "")) ? "未设置" : spProfile.getString("birthday", ""));
        tvGoalWater.setText(spProfile.getString("goal_water", "8") + " 杯");
        tvGoalSleep.setText(spProfile.getString("goal_sleep", "8") + " 小时");
        tvGoalExercise.setText(spProfile.getString("goal_exercise", "30") + " 分钟");
        tvReminderWater.setText(spProfile.getString("reminder_water", "09:00"));
        tvReminderSleep.setText(spProfile.getString("reminder_sleep", "23:00"));
        tvReminderExercise.setText(spProfile.getString("reminder_exercise", "19:00"));
    }

    private void setupExpandCollapse() {
        findViewById(R.id.headerProfile).setOnClickListener(v -> toggleSection(contentProfile, arrowProfile));
        findViewById(R.id.headerGoal).setOnClickListener(v -> toggleSection(contentGoal, arrowGoal));
        findViewById(R.id.headerReminder).setOnClickListener(v -> toggleSection(contentReminder, arrowReminder));
    }

    private void toggleSection(LinearLayout content, TextView arrow) {
        boolean visible = content.getVisibility() == View.VISIBLE;
        content.setVisibility(visible ? View.GONE : View.VISIBLE);
        arrow.setText(visible ? "▶" : "▼");
    }

    private void setupRowClickListeners() {
        findViewById(R.id.rowAge).setOnClickListener(v -> showInputDialog("年龄（岁）", "age", "20", tvAge, " 岁"));
        findViewById(R.id.rowHeight).setOnClickListener(v -> showInputDialog("身高（cm）", "height", "170", tvHeight, " cm"));
        findViewById(R.id.rowWeight).setOnClickListener(v -> showInputDialog("体重（kg）", "weight", "60", tvWeight, " kg"));
        findViewById(R.id.rowBirthday).setOnClickListener(v -> showInputDialog("生日（如 2005-01-01）", "birthday", "", tvBirthday, ""));
        findViewById(R.id.rowGoalWater).setOnClickListener(v -> showInputDialog("目标饮水（杯）", "goal_water", "8", tvGoalWater, " 杯"));
        findViewById(R.id.rowGoalSleep).setOnClickListener(v -> showInputDialog("目标睡眠（小时）", "goal_sleep", "8", tvGoalSleep, " 小时"));
        findViewById(R.id.rowGoalExercise).setOnClickListener(v -> showInputDialog("目标运动（分钟）", "goal_exercise", "30", tvGoalExercise, " 分钟"));
        findViewById(R.id.rowReminderWater).setOnClickListener(v -> showInputDialog("饮水提醒时间（如 09:00）", "reminder_water", "09:00", tvReminderWater, ""));
        findViewById(R.id.rowReminderSleep).setOnClickListener(v -> showInputDialog("睡眠提醒时间（如 23:00）", "reminder_sleep", "23:00", tvReminderSleep, ""));
        findViewById(R.id.rowReminderExercise).setOnClickListener(v -> showInputDialog("运动提醒时间（如 19:00）", "reminder_exercise", "19:00", tvReminderExercise, ""));
    }

    private void showInputDialog(String hint, String spKey, String defaultValue, TextView displayView, String unit) {
        String current = spProfile.getString(spKey, defaultValue);
        if ("未设置".equals(current)) current = "";

        EditText editText = new EditText(this);
        editText.setHint(hint);
        if (!TextUtils.isEmpty(current)) editText.setText(current);
        editText.setSingleLine(true);
        editText.setPadding(48, 32, 48, 32);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xFFF6F3FB);
        bg.setCornerRadius(getResources().getDimensionPixelSize(R.dimen.radius_input));
        editText.setBackground(bg);

        new MaterialAlertDialogBuilder(this)
                .setTitle(hint.replace("（", "\n（"))
                .setView(editText)
                .setPositiveButton("确定", (dialog, which) -> {
                    String value = editText.getText().toString().trim();
                    if (!TextUtils.isEmpty(value)) {
                        spProfile.edit().putString(spKey, value).apply();
                        displayView.setText(value + unit);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
