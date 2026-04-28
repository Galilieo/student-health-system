package com.Compus_health.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.Compus_health.common.BaseResponse;
import com.Compus_health.common.DeleteRequest;
import com.Compus_health.common.ResultUtils;
import com.Compus_health.model.dto.healthrecord.HealthRecordAddRequest;
import com.Compus_health.model.dto.healthrecord.HealthRecordQueryRequest;
import com.Compus_health.model.entity.User;
import com.Compus_health.model.vo.HealthRecordVO;
import com.Compus_health.service.HealthRecordService;
import com.Compus_health.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags="健康日志接口")
@RequestMapping("/health-record")
public class HealthRecordController {

    @Resource
    private HealthRecordService healthRecordService;

    @Resource
    private UserService userService;


    @PostMapping("/add")
    @ApiOperation("添加健康记录")
    public BaseResponse<Long> addRecord(@RequestBody HealthRecordAddRequest request) {
        Long id = healthRecordService.addRecord(request);
        return ResultUtils.success(id);
    }

    @ApiOperation("删除健康记录")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRecord(@RequestBody DeleteRequest request) {
        Boolean result = healthRecordService.deleteRecord(request.getId());
        return ResultUtils.success(result);
    }

    @ApiOperation("获取健康记录")
    @GetMapping("/get")
    public BaseResponse<HealthRecordVO> getRecordById(Long id) {
        HealthRecordVO recordVO = healthRecordService.getRecordById(id);
        return ResultUtils.success(recordVO);
    }

    @GetMapping("/list/page")
    public BaseResponse<IPage<HealthRecordVO>> listRecordByPage(HealthRecordQueryRequest request) {
        IPage<HealthRecordVO> page = healthRecordService.listRecordByPage(request);
        return ResultUtils.success(page);
    }

    @GetMapping("/list")
    @ApiOperation("获取健康记录列表")
    public BaseResponse<List<HealthRecordVO>> listRecords(String recordType, Date startDate, Date endDate,
                                                          HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<HealthRecordVO> list = healthRecordService.listRecords(loginUser.getId(), recordType, startDate, endDate);
        return ResultUtils.success(list);
    }

    @ApiOperation("获取健康记录总金额")
    @GetMapping("/sum")
    public BaseResponse<Double> sumAmountByTypeAndDate(String recordType, Date startDate, Date endDate,
                                                       HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Double sum = healthRecordService.sumAmountByTypeAndDate(loginUser.getId(), recordType, startDate, endDate);
        return ResultUtils.success(sum);
    }

    @ApiOperation("获取最新健康记录")
    @GetMapping("/latest")
    public BaseResponse<List<HealthRecordVO>> getLatestRecords(String recordType, Integer limit,
                                                               HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (limit == null) {
            limit = 10;
        }
        List<HealthRecordVO> list = healthRecordService.getLatestRecords(loginUser.getId(), recordType, limit);
        return ResultUtils.success(list);
    }
}