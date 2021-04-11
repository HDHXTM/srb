package lbw.srb.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 借款信息表
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="BorrowInfo对象", description="借款信息表")
public class BorrowInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "借款用户id")
    private Long userId;

    @ApiModelProperty(value = "借款金额")
    @Min(value = 1,message = "金额至少为1")
    @NotNull(message = "金额至少为1")
    private BigDecimal amount;

    @ApiModelProperty(value = "还款期数")
//    @Min(value = 1,message = "期限至少为1个月")
    @NotNull(message = "期数不能为空")
    private Integer period;

    @ApiModelProperty(value = "年化利率")
    @Min(value = 1,message = "年化利率至少为1")
    @NotNull(message = "年化利率至少为1")
    private BigDecimal borrowYearRate;

    @ApiModelProperty(value = "还款方式 1-等额本息 2-等额本金 3-每月还息一次还本 4-一次还本")
    @NotNull(message = "还款方式不能为空")
    private Integer returnMethod;

    @ApiModelProperty(value = "资金用途")
    @NotNull(message = "资金用途不能为空")
    private Integer moneyUse;

//    NO_AUTH(0, "未认证"),
//    CHECK_RUN(1, "审核中"),
//    CHECK_OK(2, "审核通过"),
//    CHECK_FAIL(-1, "审核不通过"),
//    FINISH(3,"已完成")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


    //扩展字段
    @ApiModelProperty(value = "姓名")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty(value = "手机")
    @TableField(exist = false)
    private String mobile;

    @ApiModelProperty(value = "其他参数")
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();
}
