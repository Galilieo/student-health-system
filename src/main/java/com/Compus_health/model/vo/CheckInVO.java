package com.Compus_health.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CheckInVO implements Serializable {

    private Long id;

    private Long userId;

    private String userName;

    private Date checkInDate;

    private Integer waterChecked;

    private Integer sleepChecked;

    private Integer exerciseChecked;

    private Integer studyChecked;

    private Integer mealChecked;

    private String remark;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}