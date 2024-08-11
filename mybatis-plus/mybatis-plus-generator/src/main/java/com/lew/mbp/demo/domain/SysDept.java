package com.lew.mbp.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 部门表
 * </p>
 *
 * @author baomidou
 * @since 2024-08-11
 */
@Getter
@Setter
@TableName("sys_dept")
@ApiModel(value = "SysDept对象", description = "部门表")
public class SysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID ")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门类型：0-默认; 1-个人；2-团队; 3-区管; 4-战区; 5-全国; ")
    private Integer deptType;

    @ApiModelProperty("部门人数")
    private Integer deptCount;

    @ApiModelProperty("父部门id")
    private String parentId;

    @ApiModelProperty("祖级列表")
    private String ancestors;

    @ApiModelProperty("显示顺序")
    private Integer orderNum;

    @ApiModelProperty("部门状态（0正常 1停用）")
    private String status;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @ApiModelProperty("创建者")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    private String updateBy;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
