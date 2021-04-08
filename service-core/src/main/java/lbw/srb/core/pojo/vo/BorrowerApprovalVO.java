package lbw.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel(description = "借款人审批")
public class BorrowerApprovalVO {

    @ApiModelProperty(value = "借款单id")
    private Long borrowerId;

    @ApiModelProperty(value = "状态（0：未认证 1：认证中 2：认证通过 -1：认证失败）")
    private Integer status;

    @ApiModelProperty(value = "身份证信息是否正确")
    private Boolean isIdCardOk;

    @ApiModelProperty(value = "房产信息是否正确")
    private Boolean isHouseOk;

    @ApiModelProperty(value = "车辆信息是否正确")
    private Boolean isCarOk;

    @ApiModelProperty(value = "基本信息积分")
    @Max(value = 100,message = "积分最大为100！")
    @Min(value = 0,message = "积分至少为0")
    private Integer infoIntegral;
}