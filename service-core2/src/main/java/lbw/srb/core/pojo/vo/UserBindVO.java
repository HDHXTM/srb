package lbw.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(description = "账户绑定")
public class UserBindVO {

    @ApiModelProperty(value = "身份证号")
    @NotBlank(message = "身份证不能为空")
    private String idCard;

    @ApiModelProperty(value = "用户姓名")
    @NotBlank(message = "用户姓名不能为空")
    private String name;

    @ApiModelProperty(value = "银行类型")
    @NotBlank(message = "银行类型不能为空")
    private String bankType;

    @ApiModelProperty(value = "银行卡号")
    @NotBlank(message = "银行卡号不能为空")
    private String bankNo;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号不能为空")
    @Pattern(message = "手机号无效",regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$")
    private String mobile;
}