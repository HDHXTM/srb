package lbw.srb.controller;

import lbw.srb.common.result.R;
import lbw.srb.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Controller
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

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    @ResponseBody
    public R uploadFile(MultipartFile file) throws IOException {
        // 上传并返回新文件名称
        String fileName = fileUploadUtils.upload(file);
        return R.ok().message("文件上传成功").data("url", fileName);
    }
}
