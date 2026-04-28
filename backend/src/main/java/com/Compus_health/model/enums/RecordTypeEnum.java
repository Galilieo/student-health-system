package com.Compus_health.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RecordTypeEnum {

    WATER("water", "饮水"),
    SLEEP("sleep", "睡眠"),
    EXERCISE("exercise", "运动"),
    STEPS("steps", "步数"),
    WEIGHT("weight", "体重"),
    BLOOD_PRESSURE("bloodPressure", "血压"),
    HEART_RATE("heartRate", "心率");

    private final String value;
    private final String description;

    RecordTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static RecordTypeEnum getEnumByValue(String value) {
        if (value == null) {
            return null;
        }
        for (RecordTypeEnum enumItem : RecordTypeEnum.values()) {
            if (enumItem.value.equals(value)) {
                return enumItem;
            }
        }
        return null;
    }

    public static List<String> getValues() {
        return Arrays.stream(RecordTypeEnum.values())
                .map(RecordTypeEnum::getValue)
                .collect(Collectors.toList());
    }
}