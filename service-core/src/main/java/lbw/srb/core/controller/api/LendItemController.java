package lbw.srb.core.controller.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.common.result.R;
import lbw.srb.core.controller.BaseController;
import lbw.srb.core.pojo.entity.LendItem;
import lbw.srb.core.pojo.vo.InvestVO;
import lbw.srb.core.service.LendItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 前端控制器
 * </p>
 */
@Api(tags = "标的的投资")
@RestController
@RequestMapping("/api/core/lendItem")
@Slf4j
public class LendItemController extends BaseController {

    @Resource
    LendItemService lendItemService;

    @ApiOperation("会员投资提交数据")
    @PostMapping("/auth/commitInvest")
    public R commitInvest(@Valid @RequestBody InvestVO investVO) {
        String formStr=lendItemService.invest(investVO,getUserId());
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("会员投资异步回调")
    @PostMapping("/notify")
    public String TZnotify() {
        Map<String, Object> check = null;
        try {
            check = check();
        } catch (Exception e) {
            return "fail";
        }
        return lendItemService.TZnotify(check);
    }

    @ApiOperation("获取列表")
    @GetMapping("/list/{lendId}")
    public R list(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long lendId) {
        return R.ok().data("list",lendItemService.findAllByLendId(lendId));
    }
}
