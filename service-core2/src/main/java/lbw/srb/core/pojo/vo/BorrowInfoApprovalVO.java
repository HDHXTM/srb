package lbw.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(description = "借款信息审批")
public class BorrowInfoApprovalVO {

    @ApiModelProperty(value = "borrow_info_id")
    private Long id;

    @ApiModelProperty(value = "状态,2过，-1不过")
    private Integer status;

//    @ApiModelProperty(value = "审批内容")
//    private String content;

    @ApiModelProperty(value = "标的名称")
    @NotBlank(message = "标的名称不能为null")
    private String title;

    @ApiModelProperty(value = "年化利率")
    @Min(value = 1,message = "年化利率至少为1")
    @NotNull(message = "年化利率至少为1")
    private BigDecimal lendYearRate;

    @ApiModelProperty(value = "平台服务费率")
    @Min(value = 1,message = "平台服务费率至少为1")
    @NotNull(message = "平台服务费率至少为1")
    private BigDecimal serviceRate;

//    @ApiModelProperty(value = "开始日期")
//    @NotBlank(message = "开始日期不能为null")
//    private String lendStartDate;

    @ApiModelProperty(value = "标的描述")
    @NotBlank(message = "标的描述不能为空")
    private String lendInfo;
}