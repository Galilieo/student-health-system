package com.Compus_health.model.dto.healthrecord;

import com.Compus_health.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthRecordQueryRequest extends PageRequest implements Serializable {

    private Long userId;

    private String recordType;

    private Date startDate;

    private Date endDate;



    private static final long serialVersionUID = 1L;
}