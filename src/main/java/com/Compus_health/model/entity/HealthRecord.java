package com.Compus_health.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName(value = "health_record")
@Data
public class HealthRecord implements Serializable {

    //健康记录
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    //用户id
    private Long userId;

    //记录类型
    private String recordType;

    //记录日期
    private Date recordDate;

    //记录时间
    private Date recordTime;

    //记录
    private Double amount;


    private String unit;

    //备注
    private String note;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //是否删除
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}