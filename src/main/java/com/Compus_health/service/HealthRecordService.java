package com.Compus_health.service;

import com.Compus_health.model.dto.healthrecord.HealthRecordQueryRequest;
import com.Compus_health.model.vo.HealthRecordVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.Compus_health.model.dto.healthrecord.HealthRecordAddRequest;
import com.Compus_health.model.entity.HealthRecord;
import java.util.Date;
import java.util.List;

public interface HealthRecordService {

    Long addRecord(HealthRecordAddRequest request);

    Boolean deleteRecord(Long id);

    IPage<HealthRecordVO> listRecordByPage(HealthRecordQueryRequest request);

    HealthRecordVO getRecordById(Long id);

    List<HealthRecordVO> listRecords(Long userId, String recordType, Date startDate, Date endDate);

    Double sumAmountByTypeAndDate(Long userId, String recordType, Date startDate, Date endDate);

    List<HealthRecordVO> getLatestRecords(Long userId, String recordType, Integer limit);

    default QueryWrapper<HealthRecord> getQueryWrapper(HealthRecordQueryRequest request) {
        QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
        if (request != null) {
            if (request.getUserId() != null) {
                queryWrapper.eq("user_id", request.getUserId());
            }
            if (request.getRecordType() != null) {
                queryWrapper.eq("record_type", request.getRecordType());
            }
            if (request.getStartDate() != null) {
                queryWrapper.ge("record_date", request.getStartDate());
            }
            if (request.getEndDate() != null) {
                queryWrapper.le("record_date", request.getEndDate());
            }
        }
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderByDesc("record_date", "record_time");
        return queryWrapper;
    }
}