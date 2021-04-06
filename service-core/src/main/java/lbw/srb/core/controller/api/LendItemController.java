//package lbw.srb.core.controller.api;
//
//
//import com.alibaba.fastjson.JSON;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lbw.srb.common.result.R;
//import lbw.srb.core.pojo.vo.InvestVO;
//import lbw.srb.core.service.LendItemService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.List;
//import java.util.Map;
//
///**
// * <p>
// * 标的出借记录表 前端控制器
// * </p>
// */
//@Api(tags = "标的的投资")
//@RestController
//@RequestMapping("/api/core/lendItem")
//@Slf4j
//public class LendItemController {
//
//    @Resource
//    LendItemService lendItemService;
//
//    @ApiOperation("会员投资提交数据")
//    @PostMapping("/auth/commitInvest")
//    public R commitInvest(@RequestBody InvestVO investVO, HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("会员投资异步回调")
//    @PostMapping("/notify")
//    public String notify(HttpServletRequest request) {
//
//    }
//
//    @ApiOperation("获取列表")
//    @GetMapping("/list/{lendId}")
//    public R list(
//            @ApiParam(value = "标的id", required = true)
//            @PathVariable Long lendId) {
//
//    }
//}
