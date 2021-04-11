package lbw.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(description = "投标信息")
public class InvestVO {

    @ApiModelProperty("标的id")
    private Long lendId;

    @NotNull(message = "投资金额不能为空")
    @DecimalMin(value = "100",message = "最少投1000元")
    @ApiModelProperty("投资金额")
    private BigDecimal investAmount;

//    //用户id
//    private Long investUserId;
//
//    //用户姓名
//    private String investName;
}