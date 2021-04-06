//package lbw.srb.core.controller.admin;
//
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lbw.srb.common.result.R;
//import lbw.srb.core.pojo.entity.Borrower;
//import lbw.srb.core.pojo.vo.BorrowerApprovalVO;
//import lbw.srb.core.pojo.vo.BorrowerDetailVO;
//import lbw.srb.core.service.BorrowerService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
//@Api(tags = "借款人管理")
//@RestController
//@RequestMapping("/admin/core/borrower")
//@Slf4j
//public class AdminBorrowerController {
//
//    @Resource
//    private BorrowerService borrowerService;
//
//    @ApiOperation("获取借款人分页列表")
//    @GetMapping("/list/{page}/{limit}")
//    public R listPage(
//            @ApiParam(value = "当前页码", required = true)
//            @PathVariable Long page,
//
//            @ApiParam(value = "每页记录数", required = true)
//            @PathVariable Long limit,
//
//            @ApiParam(value = "查询关键字", required = false)
//            @RequestParam String keyword) {
//
//    }
//
//    @ApiOperation("获取借款人信息")
//    @GetMapping("/show/{id}")
//    public R show(
//            @ApiParam(value = "借款人id", required = true)
//            @PathVariable Long id){
//;
//    }
//
//    @ApiOperation("借款额度审批")
//    @PostMapping("/approval")
//    public R approval(@RequestBody BorrowerApprovalVO borrowerApprovalVO){
//
//    }
//}
