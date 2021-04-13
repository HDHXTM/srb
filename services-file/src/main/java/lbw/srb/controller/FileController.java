package lbw.srb.controller;

import lbw.srb.common.exception.Assert;
import lbw.srb.common.exception.BusinessException;
import lbw.srb.common.result.R;
import lbw.srb.common.result.ResponseEnum;
import lbw.srb.common.util.JwtUtils;
import lbw.srb.util.FileUploadUtils;
import lbw.srb.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@Slf4j
@RequestMapping("/api/oss/file")
public class FileController {
    @Autowired
    private FileUploadUtils fileUploadUtils;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
//    @GetMapping("common/download")
//    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
//        try {
//            if (!FileUtils.checkAllowDownload(fileName)) {
//                throw new BusinessException("文件名非法！禁止下载！");
//            }
//            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
//            String filePath = PathConfig.getDownloadPath() + fileName;
//
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//            FileUtils.setAttachmentResponseHeader(response, realFileName);
//            FileUtils.writeBytes(filePath, response.getOutputStream());
//            if (delete) {
//                FileUtils.deleteFile(filePath);
//            }
//        } catch (Exception e) {
//            log.error("下载文件失败", e);
//        }
//    }
    @GetMapping("/img/{imgUrl}")
    public void test(HttpServletResponse response,@PathVariable("imgUrl") String imageUrl) throws IOException {
        //写给浏览器
        response.setContentType("image/jpeg");
        //浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        BufferedImage buffImg = ImageIO.read(new FileInputStream("D:\\study\\java\\mySrb\\services-file\\file\\"+imageUrl));
        String[] split = imageUrl.split("\\.");
        ImageIO.write(buffImg,split[1] ,response.getOutputStream());
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/auth/upload")
    public R uploadFile(MultipartFile file){
        // 上传并返回新文件名称
        String fileName;
        try {
            fileName = fileUploadUtils.upload(file);
        } catch (BusinessException e) {
            return R.error().message(e.getMessage()).code(e.getCode());
        } catch (IOException e) {
            return R.error().message(e.getMessage());
        }
        return R.ok().message("文件上传成功").data("url", fileName);
    }

//    @DeleteMapping("/remove")
//    public R remove(String url, HttpServletRequest request){
//        String token = request.getHeader("token");
//        JwtUtils.checkToken(token);
//        if (!FileUtils.checkAllowDownload(url))
//            throw new BusinessException("文件名非法！禁止删除！");
//        if(FileUtils.deleteFile(url))
//            return R.ok();
//        return R.error().message("删除失败");
//    }
}
