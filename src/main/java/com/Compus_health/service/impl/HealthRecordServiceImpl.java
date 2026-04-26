package com.Compus_health.service.impl;

import com.Compus_health.model.vo.HealthRecordVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Compus_health.mapper.HealthRecordMapper;
import com.Compus_health.mapper.UserMapper;
import com.Compus_health.model.dto.healthrecord.HealthRecordAddRequest;
import com.Compus_health.model.dto.healthrecord.HealthRecordQueryRequest;
import com.Compus_health.model.entity.HealthRecord;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.enums.RecordTypeEnum;
import com.Compus_health.service.HealthRecordService;
import com.Compus_health.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HealthRecordServiceImpl extends ServiceImpl<HealthRecordMapper, HealthRecord> implements HealthRecordService {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Override
    public Long addRecord(HealthRecordAddRequest request) {
        User loginUser = userService.getLoginUser(null);

        HealthRecord record = new HealthRecord();
        record.setUserId(loginUser.getId());
        record.setRecordType(request.getRecordType());
        record.setRecordDate(request.getRecordDate() != null ? request.getRecordDate() : new Date());
        record.setRecordTime(request.getRecordTime());
        record.setAmount(request.getAmount());
        record.setUnit(request.getUnit());
        record.setNote(request.getNote());

        boolean saveResult = this.save(record);
        if (!saveResult) {
            throw new RuntimeException("记录添加失败");
        }
        return record.getId();
    }

    @Override
    public Boolean deleteRecord(Long id) {
        HealthRecord record = this.getById(id);
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }
        User loginUser = userService.getLoginUser(null);
        if (!record.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限删除");
        }
        return this.removeById(id);
    }

    @Override
    public IPage<HealthRecordVO> listRecordByPage(HealthRecordQueryRequest request) {
        User loginUser = userService.getLoginUser(null);
        Long userId = request.getUserId();
        if (userId == null) {
            userId = loginUser.getId();
        }
        if (!userId.equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new RuntimeException("无权限查看");
        }

        Page<HealthRecord> page = new Page<>(request.getCurrent(), request.getPageSize());
        QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq(request.getRecordType() != null, "record_type", request.getRecordType());
        queryWrapper.ge(request.getStartDate() != null, "record_date", request.getStartDate());
        queryWrapper.le(request.getEndDate() != null, "record_date", request.getEndDate());
        queryWrapper.orderByDesc("record_date", "record_time");
        IPage<HealthRecord> recordPage = this.page(page, queryWrapper);

        return recordPage.convert(this::getHealthRecordVO);
    }

    @Override
    public HealthRecordVO getRecordById(Long id) {
        HealthRecord record = this.getById(id);
        if (record == null) {
            return null;
        }
        return getHealthRecordVO(record);
    }

    @Override
    public List<HealthRecordVO> listRecords(Long userId, String recordType, Date startDate, Date endDate) {
        QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq(recordType != null, "record_type", recordType);
        queryWrapper.ge(startDate != null, "record_date", startDate);
        queryWrapper.le(endDate != null, "record_date", endDate);
        queryWrapper.orderByDesc("record_date", "record_time");
        List<HealthRecord> recordList = this.list(queryWrapper);
        return recordList.stream().map(this::getHealthRecordVO).collect(Collectors.toList());
    }

    @Override
    public Double sumAmountByTypeAndDate(Long userId, String recordType, Date startDate, Date endDate) {
        QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("COALESCE(SUM(amount), 0) AS totalAmount");
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("record_type", recordType);
        queryWrapper.between("record_date", startDate, endDate);
        List<Map<String, Object>> resultList = this.baseMapper.selectMaps(queryWrapper);
        if (resultList == null || resultList.isEmpty()) {
            return 0D;
        }
        Object totalAmount = resultList.get(0).get("totalAmount");
        if (totalAmount instanceof Number) {
            return ((Number) totalAmount).doubleValue();
        }
        return totalAmount == null ? 0D : Double.valueOf(totalAmount.toString());
    }

    @Override
    public List<HealthRecordVO> getLatestRecords(Long userId, String recordType, Integer limit) {
        QueryWrapper<HealthRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq(recordType != null, "record_type", recordType);
        queryWrapper.orderByDesc("record_date", "record_time");
        int safeLimit = (limit == null || limit <= 0) ? 10 : limit;
        queryWrapper.last("LIMIT " + safeLimit);
        List<HealthRecord> recordList = this.list(queryWrapper);
        if (recordList == null) {
            return Collections.emptyList();
        }
        return recordList.stream().map(this::getHealthRecordVO).collect(Collectors.toList());
    }

    private HealthRecordVO getHealthRecordVO(HealthRecord record) {
        HealthRecordVO vo = new HealthRecordVO();
        BeanUtils.copyProperties(record, vo);
        User user = userMapper.selectById(record.getUserId());
        if (user != null) {
            vo.setUserName(user.getUserName());
        }
        RecordTypeEnum enumItem = RecordTypeEnum.getEnumByValue(record.getRecordType());
        if (enumItem != null) {
            vo.setRecordTypeName(enumItem.getDescription());
        }
        return vo;
    }
}