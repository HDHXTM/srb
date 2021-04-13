package lbw.srb.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户登录记录表
 * </p>
 *
 * @author Helen
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UserLoginRecord对象", description="用户登录记录表")
public class UserLoginRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public UserLoginRecord(Long userId, String ip) {
        this.userId = userId;
        this.ip = ip;
    }

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "ip")
    private String ip;

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


}
