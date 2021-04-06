package lbw.srb.core.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.common.result.R;
import lbw.srb.core.pojo.entity.IntegralGrade;
import lbw.srb.core.service.IntegralGradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 */
@Api(tags = "积分等级管理")
@RestController
@RequestMapping("/admin/core/integralGrade")
@Slf4j
public class AdminIntegralGradeController {
    @Autowired
    IntegralGradeService integralGradeService;
    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public R listAll(){
        QueryWrapper<IntegralGrade> wrapper = new QueryWrapper<>();
//        不能带更新，创建时间，不然updateTime就更新不了，，
        wrapper.select("id","integralStart","integralEnd","borrowAmount");
        return R.ok().data("list",integralGradeService.list(wrapper));
    }

    @ApiOperation(value = "根据id删除数据记录", notes="逻辑删除数据记录")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(value = "数据id", example = "100", required = true)
            @PathVariable Long id){
        if(integralGradeService.removeById(id))
            return R.ok().message("删除成功");
        return R.error().message("删除失败");
    }
//
    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象", required = true)
            @Valid @RequestBody IntegralGrade integralGrade){
        if(integralGradeService.save(integralGrade))
            return R.ok().message("添加成功");
        return R.error().message("添加失败");
    }
//
    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(value = "数据id", required = true, example = "1")
            @PathVariable Long id){
        return R.ok().data("record",integralGradeService.getById(id));
    }
//
    @ApiOperation("更新积分等级")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(value = "积分等级对象", required = true)
            @Valid @RequestBody IntegralGrade integralGrade){
        integralGradeService.updateById(integralGrade);
        return R.ok().message("成功");
    }
}

