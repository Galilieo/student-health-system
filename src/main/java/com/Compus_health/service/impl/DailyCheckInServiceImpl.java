package com.Compus_health.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Compus_health.mapper.DailyCheckInMapper;
import com.Compus_health.mapper.UserMapper;
import com.Compus_health.model.dto.checkin.CheckInAddRequest;
import com.Compus_health.model.dto.checkin.CheckInQueryRequest;
import com.Compus_health.model.entity.DailyCheckIn;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.CheckInVO;
import com.Compus_health.service.DailyCheckInService;
import com.Compus_health.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyCheckInServiceImpl extends ServiceImpl<DailyCheckInMapper, DailyCheckIn> implements DailyCheckInService {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Override
    public Long addCheckIn(CheckInAddRequest request) {
        User loginUser = userService.getLoginUser(null);
        
        Date today = new Date();
        QueryWrapper<DailyCheckIn> todayQueryWrapper = new QueryWrapper<>();
        todayQueryWrapper.eq("user_id", loginUser.getId());
        todayQueryWrapper.eq("check_in_date", today);
        DailyCheckIn existingCheckIn = this.getOne(todayQueryWrapper, false);
        if (existingCheckIn != null) {
            throw new RuntimeException("今日已打卡");
        }

        DailyCheckIn checkIn = new DailyCheckIn();
        checkIn.setUserId(loginUser.getId());
        checkIn.setCheckInDate(today);
        checkIn.setWaterChecked(request.getWaterChecked());
        checkIn.setSleepChecked(request.getSleepChecked());
        checkIn.setExerciseChecked(request.getExerciseChecked());
        checkIn.setStudyChecked(request.getStudyChecked());
        checkIn.setMealChecked(request.getMealChecked());
        checkIn.setRemark(request.getRemark());

        boolean saveResult = this.save(checkIn);
        if (!saveResult) {
            throw new RuntimeException("打卡失败");
        }
        return checkIn.getId();
    }

    @Override
    public Boolean deleteCheckIn(Long id) {
        DailyCheckIn checkIn = this.getById(id);
        if (checkIn == null) {
            throw new RuntimeException("打卡记录不存在");
        }
        User loginUser = userService.getLoginUser(null);
        if (!checkIn.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限删除");
        }
        return this.removeById(id);
    }

    @Override
    public IPage<CheckInVO> listCheckInByPage(CheckInQueryRequest request) {
        User loginUser = userService.getLoginUser(null);
        Long userId = request.getUserId();
        if (userId == null) {
            userId = loginUser.getId();
        }
        if (!userId.equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限查看");
        }

        Page<DailyCheckIn> page = new Page<>(request.getCurrent(), request.getPageSize());
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ge(request.getStartDate() != null, "check_in_date", request.getStartDate());
        queryWrapper.le(request.getEndDate() != null, "check_in_date", request.getEndDate());
        queryWrapper.orderByDesc("check_in_date");
        IPage<DailyCheckIn> checkInPage = this.page(page, queryWrapper);

        return checkInPage.convert(this::getCheckInVO);
    }

    @Override
    public CheckInVO getCheckInById(Long id) {
        DailyCheckIn checkIn = this.getById(id);
        if (checkIn == null) {
            return null;
        }
        return getCheckInVO(checkIn);
    }

    @Override
    public DailyCheckIn getTodayCheckIn(Long userId) {
        Date today = new Date();
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("check_in_date", today);
        return this.getOne(queryWrapper, false);
    }

    @Override
    public Integer countCheckInsByMonth(Long userId, Date startDate, Date endDate) {
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.between("check_in_date", startDate, endDate);
        return Math.toIntExact(this.count(queryWrapper));
    }

    @Override
    public List<CheckInVO> listCheckIns(Long userId, Date startDate, Date endDate) {
        QueryWrapper<DailyCheckIn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ge(startDate != null, "check_in_date", startDate);
        queryWrapper.le(endDate != null, "check_in_date", endDate);
        queryWrapper.orderByDesc("check_in_date");
        List<DailyCheckIn> checkInList = this.list(queryWrapper);
        return checkInList.stream().map(this::getCheckInVO).collect(Collectors.toList());
    }

    private CheckInVO getCheckInVO(DailyCheckIn checkIn) {
        CheckInVO vo = new CheckInVO();
        BeanUtils.copyProperties(checkIn, vo);
        User user = userMapper.selectById(checkIn.getUserId());
        if (user != null) {
            vo.setUserName(user.getUserName());
        }
        return vo;
    }
}