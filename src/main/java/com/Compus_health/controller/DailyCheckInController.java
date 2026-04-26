package com.Compus_health.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.Compus_health.common.BaseResponse;
import com.Compus_health.common.DeleteRequest;
import com.Compus_health.common.ResultUtils;
import com.Compus_health.model.dto.checkin.CheckInAddRequest;
import com.Compus_health.model.dto.checkin.CheckInQueryRequest;
import com.Compus_health.model.entity.DailyCheckIn;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.CheckInVO;
import com.Compus_health.service.DailyCheckInService;
import com.Compus_health.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/checkin")
public class DailyCheckInController {

    @Resource
    private DailyCheckInService dailyCheckInService;

    @Resource
    private UserService userService;

    @PostMapping("/add")
    public BaseResponse<Long> addCheckIn(@RequestBody CheckInAddRequest request) {
        Long id = dailyCheckInService.addCheckIn(request);
        return ResultUtils.success(id);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCheckIn(@RequestBody DeleteRequest request) {
        Boolean result = dailyCheckInService.deleteCheckIn(request.getId());
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<CheckInVO> getCheckInById(Long id) {
        CheckInVO checkInVO = dailyCheckInService.getCheckInById(id);
        return ResultUtils.success(checkInVO);
    }

    @GetMapping("/today")
    public BaseResponse<DailyCheckIn> getTodayCheckIn(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        DailyCheckIn checkIn = dailyCheckInService.getTodayCheckIn(loginUser.getId());
        return ResultUtils.success(checkIn);
    }

    @GetMapping("/list/page")
    public BaseResponse<IPage<CheckInVO>> listCheckInByPage(CheckInQueryRequest request) {
        IPage<CheckInVO> page = dailyCheckInService.listCheckInByPage(request);
        return ResultUtils.success(page);
    }

    @GetMapping("/list/range")
    public BaseResponse<List<CheckInVO>> listCheckIns(Date startDate, Date endDate, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<CheckInVO> list = dailyCheckInService.listCheckIns(loginUser.getId(), startDate, endDate);
        return ResultUtils.success(list);
    }

    @GetMapping("/count/month")
    public BaseResponse<Integer> countCheckInsByMonth(Date startDate, Date endDate, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Integer count = dailyCheckInService.countCheckInsByMonth(loginUser.getId(), startDate, endDate);
        return ResultUtils.success(count);
    }
}