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

/**
 * <p>
 * 积分等级表
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="IntegralGrade对象", description="积分等级表")
public class IntegralGrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "积分区间开始")
    @Min(value = 0,message = "积分区间开始至少为0！")
    @NotNull(message = "积分区间不能为空")
    private Integer integralStart;

    @ApiModelProperty(value = "积分区间结束")
    @Min(value = 1,message = "积分区间结束至少为1！")
    @NotNull(message = "积分区间不能为空")
    private Integer integralEnd;

    @ApiModelProperty(value = "借款额度")
    @NotNull(message = "借款额度不能为空")
    private BigDecimal borrowAmount;

    @ApiModelProperty(value = "创建时间", example = "2021-01-01 08:00:00")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", example = "2021-01-01 08:00:00")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0:未删除)")
    @TableField("is_deleted")
    @TableLogic
    private Boolean deleted;


}
