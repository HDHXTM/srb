package lbw.srb.core.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.common.result.R;
import lbw.srb.core.controller.BaseController;
import lbw.srb.core.pojo.entity.Lend;
import lbw.srb.core.service.LendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 标的准备表 前端控制器
 * </p>
 */
@Api(tags = "标的管理")
@RestController
@RequestMapping("/admin/core/lend")
@Slf4j
public class AdminLendController extends BaseController {
    @Autowired
    private LendService lendService;

    @ApiOperation("标的列表")
    @GetMapping("/list")
    public R list() {
        return R.ok().data("list",lendService.findAll());
    }

    @ApiOperation("获取标的信息")
    @GetMapping("/show/{id}")
    public R show(
            @ApiParam(value = "标的id", required = true)
            @PathVariable Long id) {
        return R.ok().data("lendDetail", lendService.getLendDetail(id));
    }

    @ApiOperation("放款")
    @GetMapping("/makeLoan/{id}")
    public R makeLoan(
            @ApiParam(value = "标的id", required = true)
            @PathVariable("id") Long id) {
        lendService.makeLoan(id,getUserId());
        return R.ok().message("成功");
    }
}
