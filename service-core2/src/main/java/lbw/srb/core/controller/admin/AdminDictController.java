package lbw.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lbw.srb.common.result.R;
import lbw.srb.core.pojo.entity.Dict;
import lbw.srb.core.pojo.excelListener.DictListener;
import lbw.srb.core.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 */
@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/core/dict")
@Slf4j
public class AdminDictController {

    @Autowired
    private DictService dictService;

    @ApiOperation("Excel数据的批量导入")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel数据字典文件", required = true)
            @RequestParam("file") MultipartFile file){
        try {
            EasyExcel.read(file.getInputStream(), Dict.class, new DictListener(dictService)).sheet().doRead();
            return R.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return R.error().message("error");
        }
    }

    @ApiOperation("Excel数据的导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), Dict.class).sheet("sheet1").doWrite(dictService.list());
    }


    /*树形数据的两种加载方案
    方案一：非延迟加载
    需要后端返回的数据结构中包含嵌套数据，并且嵌套数据放在children属性中
    方案二：延迟加载
    不需要后端返回数据中包含嵌套数据，并且要定义布尔属性hasChildren，表示当前节点是否包含子数据
    如果hasChildren为true，就表示当前节点包含子数据
    如果hasChildren为false，就表示当前节点不包含子数据
    如果当前节点包含子数据，那么点击当前节点的时候，就需要通过load方法加载子数据*/
    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "上级节点id", required = true)
            @PathVariable Long parentId){
        return R.ok().data("list",dictService.listByParentId(parentId));
    }
}

