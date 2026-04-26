package com.Compus_health.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HealthRecordVO implements Serializable {

    private Long id;

    private Long userId;

    private String userName;

    private String recordType;

    private String recordTypeName;

    private Date recordDate;

    private Date recordTime;

    private Double amount;

    private String unit;

    private String note;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}