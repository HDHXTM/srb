package lbw.srb.core.pojo.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description="借款人信息详情")
public class BorrowerDetailVO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "是否结婚")
    private String marry;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "月收入")
    private String income;

    @ApiModelProperty(value = "还款来源")
    private String returnSource;

    @ApiModelProperty(value = "联系人名称")
    private String contactsName;

    @ApiModelProperty(value = "联系人手机")
    private String contactsMobile;

    @ApiModelProperty(value = "联系人关系")
    private String contactsRelation;

    @ApiModelProperty(value = "审核状态（0：未认证 1：认证中 2：认证通过 -1：认证失败）")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "借款人附件资料")
    private List<BorrowerAttachVO> borrowerAttachVOList;
}