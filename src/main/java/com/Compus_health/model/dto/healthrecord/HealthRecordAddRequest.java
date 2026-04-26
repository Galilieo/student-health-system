package com.Compus_health.model.dto.healthrecord;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HealthRecordAddRequest implements Serializable {

    private String recordType;

    private Date recordDate;

    private Date recordTime;

    private Double amount;

    private String unit;

    private String note;

    private static final long serialVersionUID = 1L;
}