package lbw.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lbw.srb.core.pojo.entity.BorrowerAttach;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel(description="借款人认证信息")
public class BorrowerVO {

    @ApiModelProperty(value = "性别（1：男 0：女）")
    @NotNull(message = "性别不能为空")
    private Integer sex;

    @ApiModelProperty(value = "年龄")
    @NotNull(message = "性别不能为空")
    private Integer age;

    @ApiModelProperty(value = "学历")
    @NotNull(message = "学历不能为空")
    private Integer education;

    @ApiModelProperty(value = "是否结婚（1：是 0：否）")
    @NotNull(message = "婚否不能为空")
    private Boolean marry;

    @ApiModelProperty(value = "行业")
    @NotNull(message = "行业不能为空")
    private Integer industry;

    @ApiModelProperty(value = "月收入")
    @NotNull(message = "月收入不能为空")
    private Integer income;

    @ApiModelProperty(value = "还款来源")
    @NotNull(message = "还款来源不能为空")
    private Integer returnSource;

    @ApiModelProperty(value = "联系人名称")
    @NotBlank(message = "联系人名称不能为空")
    private String contactsName;

    @ApiModelProperty(value = "联系人手机")
    @NotBlank(message = "联系人手机不能为空")
    @Pattern(message = "联系人手机无效",regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$")
    private String contactsMobile;

    @ApiModelProperty(value = "联系人关系")
    @NotNull(message = "联系人关系不能为空")
    private Integer contactsRelation;

    @ApiModelProperty(value = "借款人附件资料")
    private List<BorrowerAttach> borrowerAttachList;
}