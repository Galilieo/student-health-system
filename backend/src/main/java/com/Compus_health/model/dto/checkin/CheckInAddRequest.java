package com.Compus_health.model.dto.checkin;

import lombok.Data;

import java.io.Serializable;

@Data
public class CheckInAddRequest implements Serializable {

    private Integer waterChecked;

    private Integer sleepChecked;

    private Integer exerciseChecked;

    private Integer studyChecked;

    private Integer mealChecked;

    private String remark;

    private static final long serialVersionUID = 1L;
}