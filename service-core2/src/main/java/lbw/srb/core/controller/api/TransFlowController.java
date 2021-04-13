package lbw.srb.core.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lbw.srb.common.result.R;
import lbw.srb.core.controller.BaseController;
import lbw.srb.core.pojo.entity.TransFlow;
import lbw.srb.core.service.TransFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 交易流水表 前端控制器
 * </p>
 */
@Api(tags = "资金记录")
@RestController
@RequestMapping("/api/core/transFlow")
@Slf4j
public class TransFlowController extends BaseController {

    @Resource
    private TransFlowService transFlowService;

    @ApiOperation("获取列表")
    @GetMapping("/auth/list")
    public R list() {
        return R.ok().data("list",transFlowService.findAllByUserId(getUserId()));
    }
}
