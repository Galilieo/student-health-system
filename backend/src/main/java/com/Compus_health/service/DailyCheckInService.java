package com.Compus_health.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.Compus_health.model.dto.checkin.CheckInAddRequest;
import com.Compus_health.model.dto.checkin.CheckInQueryRequest;
import com.Compus_health.model.entity.DailyCheckIn;
import com.Compus_health.model.vo.CheckInVO;

import java.util.Date;
import java.util.List;

public interface DailyCheckInService {

    Long addCheckIn(CheckInAddRequest request);

    Boolean deleteCheckIn(Long id);

    IPage<CheckInVO> listCheckInByPage(CheckInQueryRequest request);

    CheckInVO getCheckInById(Long id);

    DailyCheckIn getTodayCheckIn(Long userId);

    Integer countCheckInsByMonth(Long userId, Date startDate, Date endDate);

    List<CheckInVO> listCheckIns(Long userId, Date startDate, Date endDate);

    default QueryWrapper<DailyCheckIn> getQueryWrapper(CheckInQueryRequest request) {
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        if (request != null) {
            if (request.getUserId() != null) {
                queryWrapper.eq("user_id", request.getUserId());
            }
            if (request.getStartDate() != null) {
                queryWrapper.ge("check_in_date", request.getStartDate());
            }
            if (request.getEndDate() != null) {
                queryWrapper.le("check_in_date", request.getEndDate());
            }
        }
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderByDesc("check_in_date");
        return queryWrapper;
    }
}