package lbw.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.common.result.R;
import lbw.srb.core.controller.BaseController;
import lbw.srb.core.pojo.entity.LendReturn;
import lbw.srb.core.service.LendReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 还款记录表 前端控制器
 * </p>
 */
@Api(tags = "还款计划")
@RestController
@RequestMapping("/api/core/lendReturn")
@Slf4j
public class LendReturnController extends BaseController {

    @Resource
    private LendReturnService lendReturnService;

    @ApiOperation("获取列表")
    @GetMapping("/list/{lendId}")
    public R list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId) {
        return R.ok().data("list",lendReturnService.findAllByLendId(lendId));
    }

    @ApiOperation("用户还款")
    @PostMapping("/auth/commitReturn/{lendReturnId}")
    public R commitReturn(
            @ApiParam(value = "还款计划id", required = true)
            @PathVariable Long lendReturnId) {
        return R.ok().data("formStr", lendReturnService.commitReturn(lendReturnId,getUserId()));
    }

    @ApiOperation("还款异步回调")
    @PostMapping("/notifyUrl")
    public String notifyUrl() {
        try {
            Map<String, Object> map = check();
            return lendReturnService.HQnotify(map);
        } catch (Exception e) {
            return "fail";
        }
    }
}

